package com.masterarbeit.extractdocument;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
/**
 * Main class for extracting the contents from a file
 * */
public class DocumentExtract {
    private HashMap<String, List<DocumentFolderFormat>> submissionDocuments = new HashMap<String, List<DocumentFolderFormat>>();
    public HashMap<String, List<DocumentFolderFormat>> content(File zip, Integer indexType) throws IOException {
        try {
            ZipFile zipFile = new ZipFile(zip);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while(entries.hasMoreElements()){
                ZipEntry entry = entries.nextElement();
                /** It will first filter some of specific endings that are not relevant for the calculation */
                if ((  entry.getName().endsWith(".js") || entry.getName().endsWith(".java"))
                        && !entry.getName().contains("__MACOSX") && !entry.getName().contains("node_modules")
                        &&
                        (!Pattern.compile(Pattern.quote("fontawesome"), Pattern.CASE_INSENSITIVE).matcher(entry.toString()).find()
                        && !entry.getName().contains(".DS_Store")
                        && !entry.getName().contains(".min.")
                        && !entry.getName().contains("tailwind")
                        && !entry.getName().contains("bootstrap") && !entry.getName().contains("material"))) {
                    /** Pattern matching to get the folder's name */
                    Pattern folderPattern = Pattern.compile("\\/(a\\d{1,2})\\/");
                    Matcher matchFolderName = folderPattern.matcher(entry.getName());
                    String specificFolderName = "";
                    if(matchFolderName.find()){
                        specificFolderName = matchFolderName.group(1);
                        System.out.println(entry.getName() + "   " + specificFolderName);
                    }
                    /** Pattern matching to get filename */
                    Pattern pattern = Pattern.compile("^(.+)\\/([^\\/]+)$");
                    Matcher match = pattern.matcher(entry.getName());
                    String[] parts;
                    if (match.matches()) {
                        parts = match.group(1).split("/");
                        if (specificFolderName.equals("")) {
                            specificFolderName = parts[2];
                        }
                        InputStream stream2 = zipFile.getInputStream(entry);
                        byte[] fileBytes = stream2.readAllBytes();
                        String fileType = match.group(2).endsWith(".js") ? "js" : "java";
                        System.out.println(fileType);

                        List<String> normalizedText = new DocumentNormalizer().setText(new String(fileBytes, StandardCharsets.UTF_8), fileType);
                        DocumentFileFormat fileKeyValue = new DocumentFileFormat();
                        fileKeyValue.setFileName(match.group(2));
                        fileKeyValue.setFingerprints(normalizedText);

                        DocumentFolderFormat folder = new DocumentFolderFormat();
                        folder.setFolderName(specificFolderName);
                        folder.setFiles(Arrays.asList(fileKeyValue));
                        System.out.println("parts:  "+ parts[indexType]+" filename : "+ match.group(2));
                        if (submissionDocuments.containsKey(parts[indexType])) {
                            AtomicReference<Boolean> fileExist = new AtomicReference<>(false);
                            submissionDocuments.get(parts[indexType]).forEach(e -> {
                                if (e.getFolderName().equals(folder.getFolderName())) {
                                    fileExist.set(true);
                                    List<DocumentFileFormat> newAdditionalFiles = Stream.of(e.getFiles(), folder.getFiles()).flatMap(Collection::stream).collect(Collectors.toList());
                                    e.setFiles(newAdditionalFiles);
                                }
                            });
                            if (fileExist.get() == false) {
                                List<DocumentFolderFormat> fileEntries = Stream.of(submissionDocuments.get(parts[indexType]), List.of(folder)).flatMap(Collection::stream).collect(Collectors.toList());
                                submissionDocuments.put(parts[indexType], fileEntries);
                            }
                        } else {
                            submissionDocuments.put(parts[indexType], Arrays.asList(folder));
                        }
                    }
                }
            }
        } catch (ZipException e) {
            System.out.println(e);
        } finally {
            /**
             * delete temp file
             */
            zip.delete();
        }
        return submissionDocuments;
    }


}
