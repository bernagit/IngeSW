package utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.gerarchia.Gerarchia;
import model.scambio.Scambio;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonUtil {
    private final static String directoryGerarchie = "files/gerarchie/";
    private final static String directoryScambi = "files/scambi/";

    public static void writeGerarchia(Gerarchia gerarchia) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String nomeFile = directoryGerarchie + gerarchia.getNomeRadice() + ".json";
        try (
                FileWriter writer = new FileWriter(nomeFile)
        ) {
            writer.write(gson.toJson(gerarchia));
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio della gerarchia");
        }
    }

    private static List<Path> createListOfFile(String directory) {
        List<Path> list = null;
        try (Stream<Path> files = Files.list(Paths.get(directory))) {
            list = files.collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Errore: directory files/gerarchie non presente");
        }
        return list;
    }

    public static List<Gerarchia> readGerarchie() {
        List<Gerarchia> gerarchiaList = new ArrayList<>();
        Gerarchia gerarchia;
        try {
            Reader reader;
            if (JsonUtil.createListOfFile(directoryGerarchie) == null) {
                return null;
            }
            for (Path file : JsonUtil.createListOfFile(directoryGerarchie)) {
                reader = Files.newBufferedReader(file);
                Gson gson = new Gson();
                // convert JSON file to Gerarchia
                gerarchia = gson.fromJson(reader, Gerarchia.class);
                gerarchiaList.add(gerarchia);
            }
        } catch (IOException ex) {
            System.out.println("Errore apertura file Gerarchie");
        }
        return gerarchiaList;
    }

    public static boolean checkNomeGerarchiaRipetuto(String nome) {
        List<Gerarchia> gerarchiaList = JsonUtil.readGerarchie();
        if (gerarchiaList != null) {
            for (Gerarchia g : gerarchiaList) {
                if (g.getNomeRadice().equalsIgnoreCase(nome)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static List<Gerarchia> getGerarchieLibere() {
        List<Gerarchia> gerarchiaList = new ArrayList<>();
        List<Path> filesScambi = JsonUtil.createListOfFile(directoryScambi);
        List<Path> filesGerarchie = JsonUtil.createListOfFile(directoryGerarchie);
        if (filesGerarchie == null) {
            gerarchiaList = readGerarchie();
            return gerarchiaList;
        }
        for (Path path: filesScambi){
            filesGerarchie.remove(Path.of("files/gerarchie/" + path.getFileName()));
        }
        try {
            Reader reader;
            for (Path file : filesGerarchie) {
                reader = Files.newBufferedReader(file);
                Gson gson = new Gson();
                // convert JSON file to Gerarchia
                gerarchiaList.add(gson.fromJson(reader, Gerarchia.class));
            }
        } catch (IOException ex) {
            System.out.println("Errore apertura file Gerarchie");
        }
        return gerarchiaList;
    }

    public static List<Scambio> readScambi() {
        List<Scambio> scambioList = new ArrayList<>();
        try {
            Reader reader;
            if (JsonUtil.createListOfFile(directoryScambi) == null) {
                return null;
            }
            for (Path file : JsonUtil.createListOfFile(directoryScambi)) {
                reader = Files.newBufferedReader(file);
                Gson gson = new Gson();
                // convert JSON file to Gerarchia
                scambioList.add(gson.fromJson(reader, Scambio.class));
            }
        } catch (IOException ex) {
            System.out.println("Errore apertura file Gerarchie");
        }
        return scambioList;
    }


    public static void writeScambio(Scambio scambio) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        String nomeFile = directoryScambi + scambio.getNomeGerarchiaScambio() + ".json";
        try (
                FileWriter writer = new FileWriter(nomeFile)
        ) {
            writer.write(gson.toJson(scambio));
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio della gerarchia");
        }
    }
}
