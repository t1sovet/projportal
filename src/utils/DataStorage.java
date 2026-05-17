package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import services.UniversityDatabase;

public final class DataStorage {
    private DataStorage() {
    }

    public static void save(UniversityDatabase db, String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(db);
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static UniversityDatabase load(String path) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            Object obj = ois.readObject();
            if (obj instanceof UniversityDatabase db) {
                return db;
            }
        } catch (java.io.FileNotFoundException e) {
            // This is expected on first run
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
