package swingProgram;

import javax.swing.SwingUtilities;

public class MainDeleteSWING {

	public static void main(String[] args) throws Exception {
		SwingUtilities.invokeAndWait(() ->{
			DeleteFilesFrame frame = new DeleteFilesFrame();
			frame.setTitle("Duplicate deleter");
			frame.setLocation(500, 500);
			frame.pack();
			frame.setVisible(true);
		});

	}

}
