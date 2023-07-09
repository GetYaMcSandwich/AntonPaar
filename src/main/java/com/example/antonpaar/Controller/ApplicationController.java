package com.example.antonpaar.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/application")
public class ApplicationController {

    @GetMapping
    public List<String> fileSelection() {

        List<String> files = new ArrayList<>();

        URL url = getClass().getClassLoader().getResource("placeholder.txt");
        File dir = null;

        try {

            dir = new File(url.toURI()).getParentFile();

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        for (File f : dir.listFiles(path -> path.getName().contains(".txt"))) {

            if (!f.getName().equals("placeholder.txt")) {

                files.add(f.getName());

            }

        }

        return files;

    }

    @GetMapping("/{file}/lines")
    public Map<String, Integer> processFile(@PathVariable("file") String fileName) {

        List<String> lines = new ArrayList<>();
        Map<String, Integer> stringMap = new HashMap<String, Integer>();

        try {

            lines = Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", fileName));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String string = String.join(" ", lines);
        String[] arr = string.split(" ");

        for (String s : arr) {

            if (stringMap.containsKey(s) && !s.equals("")) {

                stringMap.put(s, stringMap.get(s) + 1);

            } else {

                stringMap.put(s, 1);

            }

        }

        TreeMap<String, Integer> sortedMap = new TreeMap<>(stringMap);

        Map<String, Integer> resultMap =
                sortedMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        return resultMap;

    }

}
