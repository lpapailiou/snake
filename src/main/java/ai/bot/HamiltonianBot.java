package ai.bot;

import ai.HamiltonianPathGenerator;
import ai.bot.Bot;
import application.GamePanel;
import util.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HamiltonianBot extends Bot {
    List<int[]> staticPath = getHamPath();                 // use: path won't change if multiple games are played. will stay at least for testing
    //List<int[]> staticPath = Arrays.asList(new int[][]{{14, 15}, {13, 15}, {12, 15}, {11, 15}, {10, 15}, {10, 14}, {11, 14}, {12, 14}, {13, 14}, {14, 14}, {15, 14}, {15, 13}, {14, 13}, {13, 13}, {12, 13}, {11, 13}, {10, 13}, {10, 12}, {11, 12}, {12, 12}, {13, 12}, {13, 11}, {12, 11}, {11, 11}, {10, 11}, {9, 11}, {8, 11}, {7, 11}, {7, 12}, {7, 13}, {6, 13}, {5, 13}, {4, 13}, {3, 13}, {2, 13}, {2, 12}, {3, 12}, {4, 12}, {5, 12}, {6, 12}, {6, 11}, {6, 10}, {5, 10}, {5, 11}, {4, 11}, {4, 10}, {4, 9}, {4, 8}, {5, 8}, {5, 9}, {6, 9}, {6, 8}, {7, 8}, {7, 9}, {7, 10}, {8, 10}, {9, 10}, {10, 10}, {11, 10}, {11, 9}, {10, 9}, {9, 9}, {8, 9}, {8, 8}, {9, 8}, {10, 8}, {10, 7}, {9, 7}, {8, 7}, {7, 7}, {6, 7}, {5, 7}, {4, 7}, {4, 6}, {3, 6}, {3, 7}, {3, 8}, {3, 9}, {3, 10}, {3, 11}, {2, 11}, {2, 10}, {2, 9}, {2, 8}, {1, 8}, {1, 9}, {1, 10}, {0, 10}, {0, 9}, {0, 8}, {0, 7}, {1, 7}, {2, 7}, {2, 6}, {1, 6}, {0, 6}, {0, 5}, {0, 4}, {0, 3}, {1, 3}, {1, 4}, {1, 5}, {2, 5}, {2, 4}, {3, 4}, {3, 5}, {4, 5}, {4, 4}, {5, 4}, {5, 3}, {4, 3}, {3, 3}, {2, 3}, {2, 2}, {3, 2}, {3, 1}, {2, 1}, {1, 1}, {1, 2}, {0, 2}, {0, 1}, {0, 0}, {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}, {7, 1}, {6, 1}, {5, 1}, {4, 1}, {4, 2}, {5, 2}, {6, 2}, {6, 3}, {6, 4}, {6, 5}, {5, 5}, {5, 6}, {6, 6}, {7, 6}, {7, 5}, {7, 4}, {7, 3}, {7, 2}, {8, 2}, {8, 1}, {8, 0}, {9, 0}, {10, 0}, {11, 0}, {11, 1}, {10, 1}, {9, 1}, {9, 2}, {10, 2}, {11, 2}, {12, 2}, {12, 3}, {11, 3}, {10, 3}, {9, 3}, {8, 3}, {8, 4}, {8, 5}, {8, 6}, {9, 6}, {10, 6}, {11, 6}, {11, 7}, {11, 8}, {12, 8}, {12, 7}, {12, 6}, {13, 6}, {13, 7}, {13, 8}, {14, 8}, {14, 9}, {13, 9}, {12, 9}, {12, 10}, {13, 10}, {14, 10}, {15, 10}, {16, 10}, {16, 9}, {15, 9}, {15, 8}, {15, 7}, {14, 7}, {14, 6}, {15, 6}, {15, 5}, {14, 5}, {13, 5}, {12, 5}, {11, 5}, {10, 5}, {9, 5}, {9, 4}, {10, 4}, {11, 4}, {12, 4}, {13, 4}, {14, 4}, {14, 3}, {13, 3}, {13, 2}, {14, 2}, {14, 1}, {13, 1}, {12, 1}, {12, 0}, {13, 0}, {14, 0}, {15, 0}, {15, 1}, {15, 2}, {15, 3}, {15, 4}, {16, 4}, {16, 5}, {16, 6}, {17, 6}, {17, 7}, {16, 7}, {16, 8}, {17, 8}, {17, 9}, {17, 10}, {18, 10}, {18, 9}, {18, 8}, {19, 8}, {19, 9}, {19, 10}, {20, 10}, {20, 9}, {20, 8}, {20, 7}, {19, 7}, {18, 7}, {18, 6}, {19, 6}, {20, 6}, {20, 5}, {19, 5}, {18, 5}, {17, 5}, {17, 4}, {18, 4}, {18, 3}, {17, 3}, {16, 3}, {16, 2}, {16, 1}, {16, 0}, {17, 0}, {17, 1}, {17, 2}, {18, 2}, {18, 1}, {18, 0}, {19, 0}, {20, 0}, {20, 1}, {19, 1}, {19, 2}, {19, 3}, {19, 4}, {20, 4}, {21, 4}, {21, 5}, {21, 6}, {22, 6}, {22, 5}, {22, 4}, {23, 4}, {23, 5}, {24, 5}, {24, 4}, {24, 3}, {23, 3}, {22, 3}, {21, 3}, {20, 3}, {20, 2}, {21, 2}, {21, 1}, {21, 0}, {22, 0}, {22, 1}, {22, 2}, {23, 2}, {24, 2}, {25, 2}, {25, 1}, {24, 1}, {23, 1}, {23, 0}, {24, 0}, {25, 0}, {26, 0}, {26, 1}, {26, 2}, {27, 2}, {27, 1}, {27, 0}, {28, 0}, {29, 0}, {29, 1}, {28, 1}, {28, 2}, {29, 2}, {29, 3}, {28, 3}, {27, 3}, {26, 3}, {25, 3}, {25, 4}, {25, 5}, {26, 5}, {26, 4}, {27, 4}, {27, 5}, {27, 6}, {28, 6}, {28, 5}, {28, 4}, {29, 4}, {29, 5}, {29, 6}, {29, 7}, {28, 7}, {27, 7}, {27, 8}, {28, 8}, {29, 8}, {29, 9}, {28, 9}, {27, 9}, {26, 9}, {26, 8}, {26, 7}, {26, 6}, {25, 6}, {25, 7}, {24, 7}, {24, 6}, {23, 6}, {23, 7}, {22, 7}, {21, 7}, {21, 8}, {22, 8}, {23, 8}, {24, 8}, {25, 8}, {25, 9}, {24, 9}, {23, 9}, {22, 9}, {21, 9}, {21, 10}, {22, 10}, {23, 10}, {24, 10}, {25, 10}, {26, 10}, {27, 10}, {27, 11}, {26, 11}, {25, 11}, {24, 11}, {23, 11}, {22, 11}, {21, 11}, {20, 11}, {19, 11}, {18, 11}, {17, 11}, {16, 11}, {15, 11}, {14, 11}, {14, 12}, {15, 12}, {16, 12}, {17, 12}, {18, 12}, {19, 12}, {20, 12}, {21, 12}, {22, 12}, {23, 12}, {24, 12}, {25, 12}, {26, 12}, {27, 12}, {28, 12}, {28, 11}, {28, 10}, {29, 10}, {29, 11}, {29, 12}, {29, 13}, {29, 14}, {28, 14}, {28, 13}, {27, 13}, {26, 13}, {25, 13}, {25, 14}, {26, 14}, {27, 14}, {27, 15}, {28, 15}, {29, 15}, {29, 16}, {28, 16}, {27, 16}, {27, 17}, {26, 17}, {26, 16}, {26, 15}, {25, 15}, {24, 15}, {24, 14}, {24, 13}, {23, 13}, {23, 14}, {23, 15}, {22, 15}, {22, 14}, {22, 13}, {21, 13}, {20, 13}, {19, 13}, {18, 13}, {17, 13}, {16, 13}, {16, 14}, {17, 14}, {18, 14}, {19, 14}, {19, 15}, {18, 15}, {17, 15}, {16, 15}, {16, 16}, {17, 16}, {17, 17}, {18, 17}, {18, 16}, {19, 16}, {20, 16}, {20, 15}, {20, 14}, {21, 14}, {21, 15}, {21, 16}, {22, 16}, {23, 16}, {23, 17}, {23, 18}, {24, 18}, {24, 17}, {24, 16}, {25, 16}, {25, 17}, {25, 18}, {26, 18}, {27, 18}, {27, 19}, {28, 19}, {28, 18}, {28, 17}, {29, 17}, {29, 18}, {29, 19}, {29, 20}, {29, 21}, {29, 22}, {28, 22}, {28, 21}, {28, 20}, {27, 20}, {27, 21}, {27, 22}, {27, 23}, {28, 23}, {29, 23}, {29, 24}, {28, 24}, {27, 24}, {27, 25}, {26, 25}, {26, 24}, {26, 23}, {26, 22}, {26, 21}, {26, 20}, {26, 19}, {25, 19}, {24, 19}, {23, 19}, {22, 19}, {22, 18}, {22, 17}, {21, 17}, {20, 17}, {19, 17}, {19, 18}, {20, 18}, {21, 18}, {21, 19}, {20, 19}, {20, 20}, {21, 20}, {22, 20}, {23, 20}, {23, 21}, {22, 21}, {21, 21}, {20, 21}, {20, 22}, {21, 22}, {22, 22}, {23, 22}, {24, 22}, {24, 21}, {24, 20}, {25, 20}, {25, 21}, {25, 22}, {25, 23}, {24, 23}, {23, 23}, {23, 24}, {24, 24}, {25, 24}, {25, 25}, {24, 25}, {24, 26}, {25, 26}, {26, 26}, {27, 26}, {28, 26}, {28, 25}, {29, 25}, {29, 26}, {29, 27}, {29, 28}, {29, 29}, {28, 29}, {28, 28}, {28, 27}, {27, 27}, {27, 28}, {27, 29}, {26, 29}, {26, 28}, {26, 27}, {25, 27}, {25, 28}, {25, 29}, {24, 29}, {23, 29}, {23, 28}, {24, 28}, {24, 27}, {23, 27}, {23, 26}, {23, 25}, {22, 25}, {22, 24}, {22, 23}, {21, 23}, {20, 23}, {20, 24}, {21, 24}, {21, 25}, {21, 26}, {22, 26}, {22, 27}, {22, 28}, {22, 29}, {21, 29}, {21, 28}, {21, 27}, {20, 27}, {20, 28}, {20, 29}, {19, 29}, {19, 28}, {19, 27}, {19, 26}, {20, 26}, {20, 25}, {19, 25}, {19, 24}, {19, 23}, {19, 22}, {19, 21}, {19, 20}, {19, 19}, {18, 19}, {18, 18}, {17, 18}, {17, 19}, {17, 20}, {18, 20}, {18, 21}, {18, 22}, {18, 23}, {18, 24}, {18, 25}, {17, 25}, {17, 24}, {17, 23}, {17, 22}, {17, 21}, {16, 21}, {16, 22}, {16, 23}, {16, 24}, {16, 25}, {15, 25}, {15, 24}, {15, 23}, {15, 22}, {15, 21}, {14, 21}, {13, 21}, {13, 22}, {14, 22}, {14, 23}, {13, 23}, {13, 24}, {14, 24}, {14, 25}, {13, 25}, {12, 25}, {12, 24}, {12, 23}, {11, 23}, {10, 23}, {10, 24}, {11, 24}, {11, 25}, {10, 25}, {9, 25}, {9, 24}, {9, 23}, {8, 23}, {8, 22}, {9, 22}, {10, 22}, {11, 22}, {12, 22}, {12, 21}, {11, 21}, {11, 20}, {12, 20}, {13, 20}, {14, 20}, {15, 20}, {16, 20}, {16, 19}, {16, 18}, {16, 17}, {15, 17}, {15, 18}, {15, 19}, {14, 19}, {14, 18}, {14, 17}, {13, 17}, {13, 18}, {13, 19}, {12, 19}, {11, 19}, {10, 19}, {10, 20}, {10, 21}, {9, 21}, {8, 21}, {7, 21}, {7, 22}, {7, 23}, {6, 23}, {6, 24}, {7, 24}, {8, 24}, {8, 25}, {8, 26}, {9, 26}, {10, 26}, {11, 26}, {12, 26}, {13, 26}, {14, 26}, {15, 26}, {16, 26}, {17, 26}, {18, 26}, {18, 27}, {17, 27}, {16, 27}, {16, 28}, {17, 28}, {18, 28}, {18, 29}, {17, 29}, {16, 29}, {15, 29}, {14, 29}, {14, 28}, {15, 28}, {15, 27}, {14, 27}, {13, 27}, {13, 28}, {13, 29}, {12, 29}, {11, 29}, {11, 28}, {12, 28}, {12, 27}, {11, 27}, {10, 27}, {10, 28}, {10, 29}, {9, 29}, {9, 28}, {9, 27}, {8, 27}, {8, 28}, {8, 29}, {7, 29}, {7, 28}, {7, 27}, {6, 27}, {5, 27}, {5, 26}, {6, 26}, {7, 26}, {7, 25}, {6, 25}, {5, 25}, {4, 25}, {3, 25}, {3, 26}, {4, 26}, {4, 27}, {4, 28}, {5, 28}, {6, 28}, {6, 29}, {5, 29}, {4, 29}, {3, 29}, {2, 29}, {1, 29}, {0, 29}, {0, 28}, {0, 27}, {1, 27}, {1, 28}, {2, 28}, {3, 28}, {3, 27}, {2, 27}, {2, 26}, {1, 26}, {0, 26}, {0, 25}, {0, 24}, {0, 23}, {0, 22}, {1, 22}, {1, 23}, {1, 24}, {1, 25}, {2, 25}, {2, 24}, {3, 24}, {4, 24}, {5, 24}, {5, 23}, {4, 23}, {3, 23}, {2, 23}, {2, 22}, {2, 21}, {1, 21}, {0, 21}, {0, 20}, {1, 20}, {2, 20}, {3, 20}, {3, 21}, {3, 22}, {4, 22}, {5, 22}, {6, 22}, {6, 21}, {6, 20}, {7, 20}, {8, 20}, {9, 20}, {9, 19}, {8, 19}, {7, 19}, {6, 19}, {5, 19}, {5, 20}, {5, 21}, {4, 21}, {4, 20}, {4, 19}, {4, 18}, {5, 18}, {6, 18}, {7, 18}, {8, 18}, {9, 18}, {10, 18}, {11, 18}, {12, 18}, {12, 17}, {11, 17}, {10, 17}, {9, 17}, {8, 17}, {8, 16}, {8, 15}, {7, 15}, {7, 16}, {7, 17}, {6, 17}, {5, 17}, {4, 17}, {3, 17}, {3, 18}, {3, 19}, {2, 19}, {2, 18}, {1, 18}, {1, 19}, {0, 19}, {0, 18}, {0, 17}, {0, 16}, {0, 15}, {0, 14}, {0, 13}, {0, 12}, {0, 11}, {1, 11}, {1, 12}, {1, 13}, {1, 14}, {1, 15}, {1, 16}, {1, 17}, {2, 17}, {2, 16}, {3, 16}, {4, 16}, {5, 16}, {6, 16}, {6, 15}, {5, 15}, {4, 15}, {3, 15}, {2, 15}, {2, 14}, {3, 14}, {4, 14}, {5, 14}, {6, 14}, {7, 14}, {8, 14}, {8, 13}, {8, 12}, {9, 12}, {9, 13}, {9, 14}, {9, 15}, {9, 16}, {10, 16}, {11, 16}, {12, 16}, {13, 16}, {14, 16}, {15, 16}, {15, 15}});
    //List<int[]> _path = new ArrayList(staticPath);
    List<int[]> _path = HamiltonianPathGenerator.getPathSection(staticPath, GamePanel.getPanel().getSnake(), GamePanel.getPanel().getGoodie());
    List<int[]> snake;
    int counter = 2;

    @Override
    protected void run() {

        if (!_path.isEmpty()) {
            //_path = HamiltonianPathGenerator.getShortcut(_path, GamePanel.getPanel().getSnake().get(0), GamePanel.getPanel().getGoodie());
            running = GamePanel.move(_path.get(0));
            _path.remove(0);
            counter--;
        } else {
            running = false;
        }
        if (_path.isEmpty()) {
            //_path = new ArrayList(staticPath);
            _path = HamiltonianPathGenerator.getPathSection(staticPath, snake, GamePanel.getPanel().getGoodie());

        }
    }

    @Override
    protected List<Direction> getPath() {
        //snake = GamePanel.getPanel().getSnake();
        //return new ArrayList<>(HamiltonianPathGenerator.getHamilton(snake.get(0)));
        return null;
    }

    protected List<int[]> getHamPath() {
        snake = GamePanel.getPanel().getSnake();
        return new ArrayList<>(HamiltonianPathGenerator.getHamilton(snake.get(0)));
    }

}
