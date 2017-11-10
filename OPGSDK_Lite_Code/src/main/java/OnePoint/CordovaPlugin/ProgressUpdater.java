package OnePoint.CordovaPlugin;

import com.allatori.annotations.DoNotRename;

@DoNotRename
public interface ProgressUpdater {
	@DoNotRename
	public void updateProgress(String update, long progress, long total);

}
