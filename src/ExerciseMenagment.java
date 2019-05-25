import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ExerciseMenagment {
    public static void main(String[] args) {
        String command = "";
        int exerciseId;
        Scanner scanner = new Scanner(System.in);
        ExerciseDao exerciseDao = new ExerciseDao();
        while (!command.equalsIgnoreCase("quit")) {

            showAllExercises(exerciseDao.findAll());
            System.out.println("Wybierz jedną z opcji (add, edit, delete, quit):");
            command = scanner.next();
            if (command.equalsIgnoreCase("add")) {
                exerciseDao.create(addExercise());
            } else if (command.equalsIgnoreCase("edit")) {
                System.out.println("Podaj id zadania do edycji");
                exerciseId = getExerciseId();
                if (hasExercise(exerciseId))
                    exerciseDao.update(editExercise(exerciseDao.read(exerciseId)), exerciseId);
                else
                    System.out.println("Brak zadania o tym Id");

            } else if (command.equalsIgnoreCase("delete")) {
                System.out.println("Podaj id zadania do usuniecia");
                exerciseId = getExerciseId();
                if (hasExercise(exerciseId))
                    exerciseDao.delete(exerciseId);
                else
                    System.out.println("Brak zadania o tym Id");
            } else if (!command.equalsIgnoreCase("quit")) {
                System.out.println("Błąd! Wprowadz ponownie.");
            }
        }
    }

    private static Exercise addExercise() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj tytuł zadania: ");
        String exerciseTitle = scanner.nextLine();
        System.out.println("Podaj opis zadania");
        String exerciseDescription = scanner.nextLine();
        return new Exercise(exerciseTitle, exerciseDescription);
    }

    private static Exercise editExercise(Exercise exercise) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ustaw id zadania: ");
        int id = getExerciseId();
        while (hasExercise(id)) {
            System.out.println("Podane Id juz istnieje.Podaj jeszcze raz.");
            id = getExerciseId();
        }
        exercise.setId(id);
        System.out.println("Ustaw tytuł zadania: ");
        exercise.setTitle(scanner.nextLine());
        System.out.println("Ustaw opis zadania: ");
        exercise.setDescription(scanner.next());

        return exercise;
    }

    private static void showAllExercises(List<Exercise> exercises) {
        for (Exercise exercise : exercises) {
            System.out.printf("Id: %s, Tytuł: %s, Opis: %s\n", exercise.getId(), exercise.getTitle(), exercise.getDescription());
        }
    }


    private static Integer getExerciseId() {
        Scanner scanner = new Scanner(System.in);
        Integer exerciseId = null;
        while (exerciseId == null)
            try {
                exerciseId = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Błedna wartość!Podaj jeszcze raz.");
                scanner.next();
            }
        return exerciseId;
    }

    private static Boolean hasExercise(int exerciseId) {
        ExerciseDao exerciseDao = new ExerciseDao();
        if (exerciseDao.read(exerciseId) == null) {
            return false;
        }
        return true;
    }
}


