package com.cookpad.android;

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.Task

import com.sun.codemodel.*
import groovy.xml.Namespace


public class ViewHolderGeneratorPlugin implements Plugin<Project> {
    @Override
    public void apply(final Project project) {
        project.logger.trace("Applying ${this.class} into $project")
        project.configure(project) {
            tasks.whenTaskAdded { task ->
                configureTask(project, task)
            }
        }
    }

    private void configureTask(final Project project, final Task task) {
        def projectFlavorNames = project.android.productFlavors.collect { it.name }
        if (projectFlavorNames.size() == 0) {
            projectFlavorNames.add("")
        }
        project.android.buildTypes.all { build ->
            def buildName = build.name // e.g. "Debug", "Release"
            projectFlavorNames.each { flavorName ->
                def taskAffix = flavorName != "" ? "${flavorName.capitalize()}${buildName.capitalize()}" : buildName.capitalize()
                if (task.name == "generate${taskAffix}Sources") {
                    def flavorPath = flavorName != "" ? "${flavorName}/${buildName}" : buildName
                    project.logger.trace "Register generate${taskAffix}ViewHolders"

                    def generateViewHoldersTask = project.task("generate${taskAffix}ViewHolders") {
                        description = 'Generates ViewHolder classes from layout XMLs'

                        doLast {
                            generateViewHolders(project, flavorPath)
                        }
                    }
                    task.dependsOn(generateViewHoldersTask)
                }
            }
        }
    }

    private static String camerize(String snakeCased) {
        return snakeCased.split(/_/).collect { it.capitalize() }.join()
    }

    private static String qualifyViewClassName(String name) {
        if (name.contains(".")) { // already fully-qualified
            return name
        } else if (name == "View") {
            return "android.view.$name"
        } else {
            return "android.widget.$name"
        }
    }

    private static JExpression castIfNeeded(JType expectedType, JType exprType, JExpression expr) {
        if (expectedType == exprType) {
            return expr
        } else {
            return JExpr.cast(expectedType, expr)
        }
    }

    public static void generateViewHolders(final Project project, final String flavorPath) {
        // TODO: do not depend on the magic string "src/main"
        def src = "src/main"

        def xmlFiles = project.fileTree(dir: src, include: "**/layout*/*.xml")

        def manifestXml = new XmlSlurper().parse(project.file("$src/AndroidManifest.xml"))

        def pkg = manifestXml.('@package').toString()

        def androidNs = new Namespace("http://schemas.android.com/apk/res/android",  "android")
        def xmlParser = new XmlParser()

        def javaSourceDir = project.file("$src/java")
        javaSourceDir.mkdirs()

        def model = new JCodeModel()

        def contextType = model.ref("android.content.Context")
        def viewType = model.ref("android.view.View")
        def viewGroupType = model.ref("android.view.ViewGroup")
        def inflaterType = model.ref("android.view.LayoutInflater")

        xmlFiles.each { File layoutXml ->
            def baseName = layoutXml.getName()
            def simpleName = baseName.split(/\./)[0]
            def className = camerize(simpleName) + "ViewHolder"
            project.logger.info "Generating ${className}.java from $layoutXml"

            def vhClass = model._class(JMod.PUBLIC | JMod.FINAL, pkg + ".generated.viewholder.$className", ClassType.CLASS)

            // XXX: toString() is required to avoid IllegalStateException
            vhClass.javadoc().add("Auto-generated ViewHolder class which represents ${baseName}.".toString())

            def layout = xmlParser.parse(layoutXml)

            vhClass.field(JMod.PUBLIC | JMod.FINAL, viewType, "view")

            // ViewHolder.from(Context context, ViewGroup rootView)
            ({
                JMethod from = vhClass.method(JMod.PUBLIC | JMod.STATIC, vhClass, "from")
                JVar context = from.param(contextType, "context")
                JVar viewGroup = from.param(viewGroupType, "viewGroup")
                from.javadoc().add("Creates a viewholder, inflates $baseName, and binds the view object to the viewholder.".toString())

                JBlock body = from.body()
                JFieldRef resId = model.ref("${pkg}.R.layout").staticRef(simpleName)
                JVar inflater = body.decl(inflaterType, "inflater", inflaterType.staticInvoke("from").arg(context))
                JInvocation inflateExpr = inflater.invoke("inflate").arg(resId).arg(viewGroup).arg(JExpr.FALSE)
                body._ass
                JVar rootView = body.decl(viewType, "rootView", inflateExpr)
                JVar holder = body.decl(vhClass, "holder", JExpr._new(vhClass).arg(rootView))
                body.add(rootView.invoke("setTag").arg(holder)) // rootView.setTag(holder)
                body._return(holder)
            })()

            // ViewHolder.from(Context context, View convertView, ViewGroup rootView)
            ({
                JMethod from = vhClass.method(JMod.PUBLIC | JMod.STATIC, vhClass, "from")
                JVar context = from.param(contextType, "context")
                JVar convertView = from.param(viewType, "convertView")
                JVar viewGroup = from.param(viewGroupType, "viewGroup")
                from.javadoc().add("Same as <code>.from(context, viewGroup)</code> method but does nothing if <code>convertView</code> is supplied. It is designed to be used in <code>ListAdapter#getView()</code>.".toString())

                JBlock body = from.body()
                JConditional cond = body._if(convertView.eq(JExpr._null()))
                cond._then()._return(vhClass.staticInvoke("from").arg(context).arg(viewGroup))
                cond._else()._return(JExpr.cast(vhClass, convertView.invoke("getTag")))
            })()

            // ViewHolder(View rootView)
            JMethod ctr = vhClass.constructor(JMod.PUBLIC)
            JVar rootView = ctr.param(viewType, "rootView")

            JBlock body = ctr.body();
            body.assign(JExpr._this().ref("view"), rootView)

            Closure<Void> walk = null
            walk = { Node element ->
                def widgetId = element.attribute(androidNs.get("id"))
                if (widgetId != null) {
                    def parts = widgetId.split('/') // like '@id+/name'
                    def name = parts[1]
                    def resClass = model.ref(parts[0] == "@android:id" ? "android.R.id" : "${pkg}.R.id")

                    JClass elementType = model.ref(qualifyViewClassName(element.name()))
                    vhClass.field(JMod.PUBLIC | JMod.FINAL, elementType, name)

                    // holder = (T)findViewById(R.id.name)
                    JInvocation findViewById = rootView.invoke("findViewById").arg(resClass.staticRef(name))
                    body.assign(JExpr._this().ref(name), castIfNeeded(elementType, viewType, findViewById))

                }

                element.children().each { walk(it) }
            }

            walk(layout)
        }

        model.build(javaSourceDir)
    }
}
