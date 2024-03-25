package hei.school.soratra.service.soratra;

import hei.school.soratra.file.BucketComponent;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.time.Duration;

@AllArgsConstructor
@Service
public class SoratraService {
    @Autowired
    private BucketComponent bucketComponent;

    public File createFile(String name, File original) throws IOException {
        if (name == null || name.isEmpty() || original == null) {
            throw new IllegalArgumentException("file name or content cannot be null");
        }


        String path = "soratra/" + name + ".txt";

        File file = new File(path);

        BufferedReader reader = new BufferedReader(new FileReader(original));

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String upperCaseLine = line.toUpperCase();
            writer.write(upperCaseLine);
            writer.newLine();
        }
        reader.close();
        writer.close();

        return file;
    }
    public String uploadFile(String id, File original) throws IOException {

        String originalImagePrefix = id + "-original";
        String transformedImagePrefix = id + "-transformed";

        String originalBucketKey = "soratra/" + originalImagePrefix + "txt";
        String transformedBucketKey = "soratra/" + transformedImagePrefix + "txt";



        File file = this.createFile(id,original);

        bucketComponent.upload(original,originalBucketKey);
        bucketComponent.upload(file,transformedBucketKey);

        return bucketComponent.presign(transformedBucketKey, Duration.ofMinutes(30)).toString();
    }

    public String getOriginalSoratraUrl(String id) {
        String originalImagePrefix = id + "-original";
        String originalBucketKey = "soratra/" + originalImagePrefix + "txt";

        URL originalUrl = bucketComponent.presign(originalBucketKey, Duration.ofMinutes(20));
        return originalUrl.toString();
    }

    public String getTransformedSoratraUrl(String id) {
        String transformedImagePrefix = id + "-transformed";
        String transformedBucketKey = "soratra/" + transformedImagePrefix + "txt";
        
        URL transformedUrl = bucketComponent.presign(transformedBucketKey, Duration.ofMinutes(20));
        return transformedUrl.toString();
    }
}
