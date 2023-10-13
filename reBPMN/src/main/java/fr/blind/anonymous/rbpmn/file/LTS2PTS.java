package fr.blind.anonymous.rbpmn.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.blind.anonymous.rbpmn.automata.PTSTask;
import fr.blind.anonymous.rbpmn.automata.PTSTransition;

public class LTS2PTS {
	
	private PTSTask pts;
	private List<String> results;
	
	public LTS2PTS(PTSTask pts) {
		
		this.pts = pts;
		results = new ArrayList<>();
	}
	
	public void generate_aut_prob() {
		
		String folderPath = "";
		String latestFolderPath = this.LatestModifiedFolder(folderPath);
		String filename = this.checkFileExtension(latestFolderPath);
		
		if(filename.contains("_prob")) {
			filename = filename.replace(".aut", "_prob.aut");
		}
		
		String filepath = latestFolderPath + "/" + filename;
		try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
            out.write("des (" + this.pts.getInitState() + ", " + Integer.toString(this.pts.getNbrOfTranstions()) + ", " + Integer.toString(this.pts.getStates().size()) + ")\n");
            
            for(String sourceState: this.pts.getTransitions().keySet())
            	for(String eventName: this.pts.getTransitions().get(sourceState).keySet()) {
            		for(PTSTransition ptsTransition: this.pts.getTransitions().get(sourceState).get(eventName)) {
            			out.write("(" + sourceState + ", " + eventName + "; prob " + ptsTransition.getProbability() + ", " + ptsTransition.getTargetState() + ")\n");
            		}	
            	}
            	
            out.close();
            System.out.println("PTS!");
        } catch (IOException e) {
        }
		
		
		String bcg_filename = filename.replace(".aut", ".bcg");
		Runtime runtime = Runtime.getRuntime();
		try {
			runtime.exec("bcg_io " + filename + " " + bcg_filename);
			runtime.exec("bcg_open " + bcg_filename + " evaluator 5 formula.mcl");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String LatestModifiedFolder(String filePath) {
		
		File folder = new File(filePath); // 设置文件夹路径
        File[] files = folder.listFiles(File::isDirectory); // 获取文件夹中所有的子文件夹
        
        String latestFolderName = null;
        
        Map<Long, String> folderMap = new HashMap<>();
        for (File file : files) {
            folderMap.put(file.lastModified(), file.getName()); // 存储每个文件夹的最新修改时间和名称
        }

        if (!folderMap.isEmpty()) {
            long latestModifiedTime = Collections.max(folderMap.keySet()); // 获取最新修改时间
            latestFolderName = folderMap.get(latestModifiedTime); // 获取最新修改时间对应的文件夹名称
            System.out.println("LastestModifiedFolder：" + latestFolderName);
        } else {
            System.out.println("The folder is empty");
        }
        
        return latestFolderName;
	}
	
	private String checkFileExtension(String folderPath) {
		
		//String folderPath = "/path/to/folder"; // 设置文件夹路径
		
		String extension = ".aut"; // 设置需要检查的后缀名
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        String fileName = null;

        boolean found = false;
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(extension)) {
                found = true;
                System.out.println("Folder exists for files with " + extension + "：" + file.getName());
                fileName = file.getName();
            }
        }

        if (!found) {
            System.out.println("Folder does not contain files with " + extension + " suffix.");
        }
        
        return fileName;

	}
}
