import java.util.Random;
import java.util.Scanner;

public class BattleshipGame {
    private final String[][] board;
    private final String EMPTY = "▪";
    private final String SHIP = "◽";
    private final String BORDER = "⬛";
    private final String MISS = "\uD83D\uDD38";
    private final String HIT = "❌";
    private int shipsLeft;

    public BattleshipGame() {
        // Создаем поле 12x12 с границами, заполненными *
        board = new String[12][12];
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (i == 0 || j == 0 || i == 11 || j == 11) {
                    board[i][j] = BORDER;
                } else {
                    board[i][j] = EMPTY;
                }
            }
        }
        shipsLeft = 20; // общее количество палуб кораблей
    }

    public boolean placeShip(int x, int y, int size, boolean isVertical) {
        // Проверяем, можно ли поставить корабль на данное место
        for (int i = 0; i < size; i++) {
            int dx = isVertical ? i : 0;
            int dy = isVertical ? 0 : i;
            if (board[x + dx][y + dy] != EMPTY) {
                return false;
            }
        }
        // Если все проверки пройдены, ставим корабль
        for (int i = 0; i < size; i++) {
            int dx = isVertical ? i : 0;
            int dy = isVertical ? 0 : i;
            board[x + dx][y + dy] = SHIP;
            // Устанавливаем границы вокруг корабля
            for (int j = x + dx - 1; j <= x + dx + 1; j++) {
                for (int k = y + dy - 1; k <= y + dy + 1; k++) {
                    if (j >= 0 && j < 12 && k >= 0 && k < 12 && board[j][k] == EMPTY) {
                        board[j][k] = BORDER;
                    }
                }
            }
        }
        return true;
    }

    public boolean shoot(int x, int y) {
        if (board[x][y] == HIT || board[x][y] == MISS) {
            System.out.println("Вы уже стреляли в это поле. Пожалуйста, выберите другое поле.");
            // Запрашиваем новые координаты
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите новые координаты:");
            int newX = scanner.nextInt();
            int newY = scanner.nextInt();
            return shoot(newX, newY); // Рекурсивный вызов функции с новыми координатами
        } else if (board[x][y] == SHIP) {
            board[x][y] = HIT;
            shipsLeft--;
            if (isShipDestroyed(x, y)) {
                System.out.println("Корабль уничтожен!");
            }
            return true;
        } else if (board[x][y] == EMPTY || board[x][y] == BORDER) {
            board[x][y] = MISS;
        }
        return false;
    }

    public boolean isGameOver() {
        return shipsLeft == 0;
    }

    public void printBoard() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (i == 0 || j == 0 || i == 11 || j == 11) {
                    System.out.print(BORDER + "\t"); // выводим границы
                } else if (board[i][j] == BORDER) {
                    System.out.print(EMPTY + "\t"); // скрываем корабли и поля вокруг кораблей противника
                } else {
                    System.out.print(board[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    public void printOpponentBoard() {
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                if (i == 0 || j == 0 || i == 11 || j == 11) {
                    System.out.print(BORDER + "\t"); // выводим границы
                } else if (board[i][j] == SHIP || board[i][j] == BORDER) {
                    System.out.print(EMPTY + "\t"); // скрываем корабли и поля вокруг кораблей противника
                } else {
                    System.out.print(board[i][j] + "\t");
                }
            }
            System.out.println();
        }
    }

    public boolean isShipDestroyed(int x, int y) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] direction : directions) {
            int dx = x + direction[0];
            int dy = y + direction[1];
            if (dx >= 0 && dx < 12 && dy >= 0 && dy < 12 && board[dx][dy] == SHIP) {
                return false; // Если рядом есть другие части корабля, то корабль не уничтожен
            }
        }
        return true; // Если рядом нет других частей корабля, то корабль уничтожен
    }

    public static void main(String[] args) throws InterruptedException {
        BattleshipGame playerGame = new BattleshipGame();
        BattleshipGame computerGame = new BattleshipGame();
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int[] shipSizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

        System.out.println("Хотите расставить корабли автоматически? (да/нет)");
        String autoPlace = scanner.next();
        if (autoPlace.equals("да")) {
            // Рандомная расстановка кораблей для игрока
            for (int size : shipSizes) {
                while (true) {
                    int x = 1 + random.nextInt(10);
                    int y = 1 + random.nextInt(10);
                    boolean isVertical = random.nextBoolean();
                    if (playerGame.placeShip(x, y, size, isVertical)) {
                        break;
                    }
                }
            }
        } else {
            for (int size : shipSizes) {
                while (true) {
                    playerGame.printBoard();
                    System.out.println("Введите координаты и ориентацию для размещения корабля размером " + size + " (формат: x y v/h):");
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();
                    String orientation = scanner.next();
                    boolean isVertical = orientation.equals("v");
                    if (playerGame.placeShip(x, y, size, isVertical)) {
                        System.out.println("Корабль успешно размещен!");
                        break;
                    } else {
                        System.out.println("Невозможно разместить корабль на этих координатах. Попробуйте еще раз.");
                    }
                }
            }
        }

        // Рандомная расстановка кораблей для компьютера
        for (int size : shipSizes) {
            while (true) {
                int x = 1 + random.nextInt(10);
                int y = 1 + random.nextInt(10);
                boolean isVertical = random.nextBoolean();
                if (computerGame.placeShip(x, y, size, isVertical)) {
                    break;
                }
            }
        }

        // Стрельба после расстановки кораблей
        while (true) {
            Thread.sleep(2000);
            System.out.println("Ваше поле:");
            playerGame.printBoard();
            System.out.println("Поле противника:");
            computerGame.printOpponentBoard();
            boolean hit;
            do {
                System.out.println("Введите координаты для выстрела (формат: x y):");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                hit = computerGame.shoot(x, y);
                if (hit) {
                    System.out.println("Попадание!");
                    computerGame.printOpponentBoard();
                    if (computerGame.isGameOver()) {
                        System.out.println("Поздравляем! Вы выиграли!");
                        break;
                    }
                } else {
                    System.out.println("Промах!");
                }
            } while (hit);

            do {
                int x = 1 + random.nextInt(10);
                int y = 1 + random.nextInt(10);
                hit = playerGame.shoot(x, y);
                if (hit) {

                    System.out.println("Компьютер попал!");
                    if (playerGame.isGameOver()) {
                        System.out.println("К сожалению, вы проиграли. Попробуйте еще раз!");
                        break;
                    } else {

                    }
                } else {
                    System.out.println("Компьютер промахнулся!");
                }
            } while (hit);
        }
    }

    public void startGame() throws InterruptedException {
        BattleshipGame playerGame = new BattleshipGame();
        BattleshipGame computerGame = new BattleshipGame();
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int[] shipSizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

        System.out.println("Хотите расставить корабли автоматически? (да/нет)");
        String autoPlace = scanner.next();
        if (autoPlace.equals("да")) {
            // Рандомная расстановка кораблей для игрока
            for (int size : shipSizes) {
                while (true) {
                    int x = 1 + random.nextInt(10);
                    int y = 1 + random.nextInt(10);
                    boolean isVertical = random.nextBoolean();
                    if (playerGame.placeShip(x, y, size, isVertical)) {
                        break;
                    }
                }
            }
        } else {
            for (int size : shipSizes) {
                while (true) {
                    playerGame.printBoard();
                    System.out.println("Введите координаты и ориентацию для размещения корабля размером " + size + " (формат: x y v/h):");
                    int x = scanner.nextInt();
                    int y = scanner.nextInt();
                    String orientation = scanner.next();
                    boolean isVertical = orientation.equals("v");
                    if (playerGame.placeShip(x, y, size, isVertical)) {
                        System.out.println("Корабль успешно размещен!");
                        break;
                    } else {
                        System.out.println("Невозможно разместить корабль на этих координатах. Попробуйте еще раз.");
                    }
                }
            }
        }

        // Рандомная расстановка кораблей для компьютера
        for (int size : shipSizes) {
            while (true) {
                int x = 1 + random.nextInt(10);
                int y = 1 + random.nextInt(10);
                boolean isVertical = random.nextBoolean();
                if (computerGame.placeShip(x, y, size, isVertical)) {
                    break;
                }
            }
        }

        // Стрельба после расстановки кораблей
        while (true) {
            Thread.sleep(1500);
            System.out.println("Ваше поле:");
            playerGame.printBoard();
            System.out.println("Поле противника:");
            computerGame.printOpponentBoard();
            boolean hit;
            do {
                System.out.println("Введите координаты для выстрела (формат: x y):");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                hit = computerGame.shoot(x, y);
                if (hit) {
                    System.out.println("Попадание!");
                    computerGame.printOpponentBoard();
                    if (computerGame.isGameOver()) {
                        System.out.println("Поздравляем! Вы выиграли!");
                        break;
                    }
                } else {
                    System.out.println("Промах!");
                }
            } while (hit);

            int lastHitX = -1;
            int lastHitY = -1;
            int[] dx = {-1, 0, 1, 0};
            int[] dy = {0, 1, 0, -1};
            int direction = 0;

            do {
                int x, y;
                if (lastHitX != -1 && lastHitY != -1) {
                    x = lastHitX + dx[direction];
                    y = lastHitY + dy[direction];
                    if (x < 1 || x > 10 || y < 1 || y > 10 || playerGame.board[x][y] == MISS) {
                        direction = (direction + 1) % 4;
                        x = lastHitX + dx[direction];
                        y = lastHitY + dy[direction];
                    }
                } else {
                    x = 1 + random.nextInt(10);
                    System.out.println(x);
                    y = 1 + random.nextInt(10);
                    System.out.println(y);
                }
                hit = playerGame.shoot(x, y);
                if (hit) {
                    System.out.println("Компьютер попал!");
                    lastHitX = x;
                    lastHitY = y;
                    if (playerGame.isGameOver()) {
                        System.out.println("К сожалению, вы проиграли. Попробуйте еще раз!");
                        break;
                    }
                } else {
                    System.out.println("Компьютер промахнулся!");
                    if (lastHitX != -1 && lastHitY != -1) {
                        direction = (direction + 1) % 4;
                    } else {
                        lastHitX = -1;
                        lastHitY = -1;
                    }
                }
            } while (hit);

        }
    }
}
