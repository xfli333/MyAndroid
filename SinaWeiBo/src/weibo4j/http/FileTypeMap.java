package weibo4j.http;

/**
 * Created by IntelliJ IDEA.
 * User: Lee
 * Date: 11-1-12
 * Time: 上午10:43
 */

import java.io.File;

/**
 * File Type Map.
 */
public abstract class FileTypeMap {

    //-------------------------------------------------------------
    // Variables --------------------------------------------------
    //-------------------------------------------------------------

    /**
     * Create default file type map.
     */
    private static FileTypeMap defaultMap = new MimetypesFileTypeMap();


    //-------------------------------------------------------------
    // Initialization ---------------------------------------------
    //-------------------------------------------------------------

    /**
     * Create new file type map.
     */
    public FileTypeMap() {
    } // FileTypeMap()


    //-------------------------------------------------------------
    // Public Accessor Methods ------------------------------------
    //-------------------------------------------------------------

    /**
     * Get content type.
     *
     * @param filename Filename
     */
    public abstract String getContentType(String filename);

    /**
     * Get content type.
     *
     * @param file File source
     */
    public abstract String getContentType(File file);

    /**
     * Get default file type map.
     *
     * @returns Default file type map
     */
    public static FileTypeMap getDefaultFileTypeMap() {
        return defaultMap;
    } // getDefaultFileTypeMap()

    /**
     * Set default file type map.
     *
     * @param map New default file type map
     */
    public static void setDefaultFileTypeMap(FileTypeMap map) {
        defaultMap = map;
    } // setDefaultFileTypeMap()


} // FileTypeMap
