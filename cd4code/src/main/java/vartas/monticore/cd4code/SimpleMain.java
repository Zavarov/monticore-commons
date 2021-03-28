package vartas.monticore.cd4code;

import de.se_rwth.commons.logging.Log;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SimpleMain {

    private static final String MODELS_PATH = "models_path";
    private static final String LOCAL_MODELS_PATH = "local_models_path";
    private static final String TEMPLATES_PATH = "templates_path";
    private static final String LOCAL_TEMPLATES_PATH = "local_templates_path";
    private static final String SOURCES_DIRECTORY = "sources_directory";
    private static final String TARGET_DIRECTORY = "target_directory";

    public static void main(String[] args) throws IOException {
        assert args.length == 2;

        JSONObject config = new JSONObject(Files.readString(resolve(args[0])));

        for(int i = 1 ; i < args.length ; ++i){
            Log.info("Generating "+args[i], "Main");
            Main.main(
                    resolve(config.getString(MODELS_PATH)).toString(),
                    resolve(config.getString(LOCAL_MODELS_PATH)).toString(),
                    resolve(config.getString(TEMPLATES_PATH)).toString(),
                    resolve(config.getString(LOCAL_TEMPLATES_PATH)).toString(),
                    resolve(config.getString(SOURCES_DIRECTORY)).toString(),
                    resolve(config.getString(TARGET_DIRECTORY)).toString(),
                    args[i]
            );
        }
    }

    private static Path resolve(String path){
        return Paths.get(path.replaceAll("/", File.separator)).toAbsolutePath();
    }
}
