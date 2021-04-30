package swingProgram;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DeleteFilesFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	
	JScrollPane scrollPane = new JScrollPane();
	JTextArea area = new JTextArea();
	JButton button = new JButton("Delete duplicates");
	JLabel label = new JLabel(String.format("Unesite iz kojeg direktorija želite obrisat duplikate:   "));
	JTextField unos = new JTextField("D:\\OOP");
	
	
	
	
	
	
	public DeleteFilesFrame() {
		
		setPreferredSize(new Dimension(500,300));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		
		scrollPane.add(area);
		
		
		
		JPanel northPanel = new JPanel();
		JPanel southPanel = new JPanel();
		
		northPanel.setLayout(new BorderLayout());
		
		northPanel.add(label,BorderLayout.WEST);
		northPanel.add(unos,BorderLayout.CENTER);
		
		southPanel.add(button);
		
		add(northPanel, BorderLayout.NORTH);
		add(area, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);
		
		button.addActionListener(e ->{
			String directory = unos.getText();
			Path root = Paths.get(directory);
			DuplicateFileVisitor visitor = new DuplicateFileVisitor(); 
		try {
		        Files.walkFileTree(root, visitor);
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }
			
			Long total = visitor.getDeletedData().entrySet().stream()
				.map(s ->s.getValue())
				.mapToLong(Long::longValue)
				.sum();
			area.append(String.format("\nFreed disk space %d MB Deleted files %d", (total/1024)/1024, visitor.getDeletedData().size()));
		});
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public class DuplicateFileVisitor extends SimpleFileVisitor<Path> {

		private Map<String, Long> data = new HashMap<String, Long>();
		private Map<String, String> repo = new HashMap<String, String>();

		public Map<String, Long> getDeletedData() {
			return data;
		} // dohva�anje pobrisanih

		public Map<String, String> getDataRepo() {
			return repo;
		}
		
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
			String digest = Digest.MD5(file);
			if (repo.containsKey(digest)) {
				Path repoFile = Paths.get(repo.get(digest));
				if (file.getNameCount() >= repoFile.getNameCount()) {
					deleteFile(file, attr.size());
				} else {
					deleteFile(repoFile, attr.size());
					repo.put(digest, file.toString());
				}
			}
			else repo.put(digest, file.toString());

			return FileVisitResult.CONTINUE;
		}

		
		
		
		
		
		private void deleteFile(Path file, long size) {
			// TO DO napisati ostatak metode u kojem se spremaju podatci o brisanju

			data.put(file.normalize().toString(), size);

			//TO DO: obrisati datoteku
			try {
				area.append(file.toString() + "\n");
				Files.delete(file);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
