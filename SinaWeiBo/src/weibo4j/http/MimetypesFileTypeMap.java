package weibo4j.http;

import com.sun.activation.registries.MimeTypeFile;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-12
 * Time: 上午10:38
 */
public class MimetypesFileTypeMap extends FileTypeMap {

	//-------------------------------------------------------------
	// Variables --------------------------------------------------
	//-------------------------------------------------------------

	private static MimeTypeFile defDB		= null;
	private MimeTypeFile[]			DB			= null;
	private	static final int		PROG		= 0;
	private	static final int		HOME		= 1;
	private	static final int		SYS			= 2;
	private	static final int		JAR			= 3;
	private	static final int		DEF			= 4;
	private	static String			defaultType	= "application/octet-stream";


	//-------------------------------------------------------------
	// Initialization ---------------------------------------------
	//-------------------------------------------------------------

	/**
	 * Create MIME Types files type map with entries from stream.
	 * @param stream MIME Types file map formatted stream
	 */
	public MimetypesFileTypeMap(InputStream stream) {
		this();
		try {
			DB[PROG] = new MimeTypeFile(stream);
		} catch (Exception e) {
			DB[PROG] = null;
		}
	} // MimetypesFileTypeMap()

	/**
	 * Create default MIME Types file type map.
	 */
	public MimetypesFileTypeMap() {

		// Variables
		String		separator;
		Properties properties;

		// Initialize
		DB = new MimeTypeFile[5];
		properties = System.getProperties();
		separator = properties.getProperty("file.separator");

		// Initialize Programmed Entries
		DB[PROG] = new MimeTypeFile();

		// Initialize User MimeTypes (~/.mime.types)
		DB[HOME] = loadFile(properties.getProperty("user.home") +
						separator + ".mime.types");

		// Initialize Java MimeTypes (<java-home>/lib/mime.types)
		DB[SYS] = loadFile(properties.getProperty("java.home") +
						separator + "lib" + separator + "mime.types");

		// Initialize Resource (META-INF/mime.types)
		DB[JAR] = loadResource("META-INF" + separator + "mime.types");

		// Initialize Default Resource (META-INF/mimetypes.default)
		DB[DEF] = loadResource("META-INF" + separator + "mimetypes.default");

	} // MimetypesFileTypeMap()

	/**
	 * Create MIME Types files type map with entries from file
	 * @param mimeTypeFileName MIME Types file map formatted file
	 */
	public MimetypesFileTypeMap(String mimeTypeFileName) {
		this();
		try {
			DB[PROG] = new MimeTypeFile(mimeTypeFileName);
		} catch (Exception e) {
		}
	} // MimetypesFileTypeMap()


	//-------------------------------------------------------------
	// Public Accessor Methods ------------------------------------
	//-------------------------------------------------------------

	/**
	 * Get content type of file.
	 * @param file File to check
	 * @returns Content type, or null
	 */
	public String getContentType(File file) {
		return getContentType(file.getName());
	} // getContentType()

	/**
	 * Get content type of file.
	 * @param filename File to check
	 * @returns Content type, or null
	 */
	public synchronized String getContentType(String filename) {

		// Variables
		int				index;
		String			mimeType;
//		MimeTypeEntry	entry;
		String			ext;

		// Get Extension
		index = filename.lastIndexOf(".");
		if (index == -1) {
			return defaultType;
		}
		ext = filename.substring(index + 1);

		// Loop through each Mime Type File
		for (index = 0; index < DB.length; index++) {

			// Get Mime Type Entry
//			entry = DB[index].getMimeTypeEntry(ext);
			if (DB[index] == null) {
				continue;
			}
			mimeType = DB[index].getMIMETypeString(ext);

			// Return Mime Type
//			if (entry != null) {
//				return entry.getMimeType();
//			}
			if (mimeType != null) {
				return mimeType;
			}

		} // for: index

		return defaultType;

	} // getContentType()

	/**
	 * Programmically add a MIME Types entry.
	 * @param mime_types MIME Types formatted entry
	 */
	public synchronized void addMimeTypes(String mime_types) {
		DB[PROG].appendToRegistry(mime_types);
	} // addMimeTypes()

	/**
	 * Load MIME Type file.
	 * @param filename Name of file
	 * @returns MIME Type file
	 */
	private MimeTypeFile loadFile(String filename) {
		try {
			return new MimeTypeFile(filename);
		} catch (Exception e) {
			return null;
		}
	} // loadFile()

	/**
	 * Load MIME Type resource file.
	 * @param resource Name of resource
	 * @returns MIME Type file
	 */
	private MimeTypeFile loadResource(String resource) {

		// Variables
		InputStream	stream;

		// Get Resource Stream
		stream = getClass().getClassLoader().getResourceAsStream(resource);

		// Return MimeType File
		try {
			return new MimeTypeFile(stream);
		} catch (Exception e) {
			return null;
		}

	} // loadResource()


} 
