package fr.inria;



import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;

public class Sysml2sec {
	
	public static void main(String []args) {

		String TransformRule = "TransformRule.xml";
		String TargetFileName = "Transformed_model.xml";
		String SourceFileName="";
        String currentPath="";

		 if (args.length==1) {
            if (args[0] != null || !(args[0].isEmpty())) {
                SourceFileName = args[0];
            }
            else {
                System.out.println("Source file is empty");
                System.exit(0);
            }
        } else if (args.length==2) {
            if (args[1] != null || !(args[1].isEmpty())) {
                SourceFileName = args[0];
                TargetFileName = args[1];
            }
            else {
                System.out.println("Use default target filename");
            }
        } else if (args.length==3) {
            if (args[2] != null || !(args[2].isEmpty())) {
                SourceFileName = args[0];
                TargetFileName = args[1];
                TransformRule = args[2];
            }
            else {
                System.out.println("Use default transforme rules!");
            }
        }


		File directory = new File("");//set current
		try{
			System.out.println(directory.getCanonicalPath());//get standard path
			System.out.println(directory.getAbsolutePath());//get absolute path
			currentPath=directory.getAbsolutePath()+"/res/";
		}catch(Exception e)
		{
			e.printStackTrace();
		}

		TransformRule=currentPath+TransformRule;
		SourceFileName=currentPath+SourceFileName;
		TargetFileName=currentPath+TargetFileName;


		ScriptEngine    engine = new ScriptEngineManager().getEngineByName("javascript");

		XmlTransform transform = new XmlTransform(TransformRule, engine);
		XmlSource    source    = new XmlSource(SourceFileName, transform.getSourceElementTypes(), engine);
		transform.setSourceDocument(source.getSourceDocument());
		transform.transform();
		transform.save(TargetFileName);
	}


}














