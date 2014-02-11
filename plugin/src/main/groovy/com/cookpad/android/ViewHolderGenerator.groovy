package com.cookpad.android

import com.sun.codemodel.*
import groovy.xml.Namespace
import org.gradle.api.Project
import org.gradle.api.file.FileCollection

public class ViewHolderGenerator {
    private static final def androidNamespace =  new Namespace("http://schemas.android.com/apk/res/android",  "android")

    private final Project project;
    private final String pkg;
    private final JCodeModel model;
    private final JClass contextType;
    private final JClass viewType;
    private final JClass viewGroupType;
    private final JClass inflaterType;

    public ViewHolderGenerator(Project project, String pkg) {
        this.project = project
        this.pkg = pkg
        this.model = new JCodeModel()

        this.contextType = model.ref("android.content.Context")
        this.viewType = model.ref("android.view.View")
        this.viewGroupType = model.ref("android.view.ViewGroup")
        this.inflaterType = model.ref("android.view.LayoutInflater")

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

    public void generate(FileCollection xmlFiles) {
        def t0 = System.currentTimeMillis()
        def xmlParser = new XmlParser()

        xmlFiles.each { File layoutXml ->
            def baseName = layoutXml.getName()
            def simpleName = baseName.split(/\./)[0]
            def className = camerize(simpleName) + "ViewHolder"
            project.logger.info "Generating ${className}.java from $layoutXml"

            def vhClass = model._class(JMod.PUBLIC | JMod.FINAL, pkg + ".generated.viewholder.$className", ClassType.CLASS)

            // XXX: toString() is required to avoid IllegalStateException
            vhClass.javadoc().add("Auto-generated ViewHolder class which represents ${baseName}.\nGenerated at ${new Date()}.".toString())

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
                def widgetId = element.attribute(androidNamespace.get("id"))
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
    }

    public JCodeModel getCodeModel() {
        return model
    }
}
