package mvvm;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MVVMCreate extends AnAction {

    private Project project;
    //包名
    private String mPackageName = "";
    private String mAuthor;//作者
    private String mModuleName;//模块名称
    private String mProjectName; // 项目名称

    private enum  CodeType {
        Page, ViewModel, Model
    }


    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getData(PlatformDataKeys.PROJECT);
        mProjectName = project.getName();
        init();
        refreshProject(e);
    }

    /**
     * 刷新项目
     * @param e
     */
    private void refreshProject(AnActionEvent e) {
        e.getProject().getProjectFile().refresh(false,false);
    }

    /**
     * 初始化Dialog
     */
    private void init(){
        MVVMCreateDialog myDialog = new MVVMCreateDialog(new MVVMCreateDialog.DialogCallBack() {
            @Override
            public void ok(String author,String packageName, String moduleName) {
                mAuthor = author;
                mPackageName = packageName.replace(".", "/");;
                mModuleName = moduleName;
                createClassFiles();
                Messages.showInfoMessage(project,"create mvvm code success","提示");
            }
        });
        myDialog.pack();
        myDialog.setVisible(true);
    }

    /**
     * 生成类文件
     */
    private void createClassFiles() {
        createClassFile(CodeType.Page);
        createClassFile(CodeType.ViewModel);
        createClassFile(CodeType.Model);
    }

    /**
     * 生成mvp框架代码
     * @param codeType
     */
    private void createClassFile(CodeType codeType) {
        String fileName = "";
        String content = "";
        String appPath = getAppPath();
        switch (codeType){
            case Page:
                fileName = "TemplatePage.txt";
                content = ReadTemplateFile(fileName);
                content = dealTemplateContent(content);
                writeToFile(content, appPath + mPackageName.toLowerCase(), mModuleName.toLowerCase() + "_page.dart");
                break;
            case ViewModel:
                fileName = "TemplateViewModel.txt";
                content = ReadTemplateFile(fileName);
                content = dealTemplateContent(content);
                writeToFile(content, appPath + mPackageName.toLowerCase(), mModuleName.toLowerCase() + "_view_model.dart");
                break;
            case Model:
                fileName = "TemplateModel.txt";
                content = ReadTemplateFile(fileName);
                content = dealTemplateContent(content);
                writeToFile(content, appPath + mPackageName.toLowerCase(), mModuleName.toLowerCase() + "_model.dart");
                break;
        }
    }

    /**
     * 获取包名文件路径
     * @return
     */
    private String getAppPath(){
        String appPath = project.getBasePath() + "/lib/";
        return appPath;
    }

    /**
     * 替换模板中字符
     * @param content
     * @return
     */
    private String dealTemplateContent(String content) {
        content = content.replace("$name", mModuleName);
        content = content.replace("$mvvmViewModel",mProjectName + "/" + mPackageName + "/" + mModuleName.toLowerCase() + "_view_model.dart");
        return content;
    }

    /**
     * 获取当前时间
     * @return
     */
    public String getDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 读取模板文件中的字符内容
     * @param fileName 模板文件名
     * @return
     */
    private String ReadTemplateFile(String fileName) {
        InputStream in = null;
        in = this.getClass().getResourceAsStream("/mvvm/Template/" + fileName);
        String content = "";
        try {
            content = new String(readStream(in));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


    private byte[] readStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = inputStream.read(buffer)) != -1){
                outputStream.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            outputStream.close();
            inputStream.close();
        }

        return outputStream.toByteArray();
    }


    /**
     * 生成
     * @param content 类中的内容
     * @param classPath 类文件路径
     * @param className 类文件名称
     */
    private void writeToFile(String content, String classPath, String className) {
        try {
            File floder = new File(classPath);
            if (!floder.exists()){
                floder.mkdirs();
            }

            File file = new File(classPath + "/" + className);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 从AndroidManifest.xml文件中获取当前app的包名
     * @return
     */
    private String getPackageName() {
        String package_name = project.getBasePath() + "/lib";
        return package_name;
    }
}
