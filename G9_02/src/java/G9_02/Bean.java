package G9_02;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name = "Bean")
@ViewScoped
public class Bean implements Serializable {

    // 直跳方式所有組合
    private int[] combinations_A = {1362, 1364, 1367, 1365, 1368, 1369, 1324, 1327, 1325, 1328, 1329, 1347, 1345, 1348, 1349, 1375, 1378, 1379, 1358, 1359, 1389, 1624, 1627, 1625, 1628, 1629, 1647, 1645, 1648, 1649, 1675, 1678, 1679, 1658, 1659, 1689, 1247, 1245, 1248, 1249, 1275, 1278, 1279, 1258, 1259, 1289, 1475, 1478, 1479, 1458, 1459, 1489, 1758, 1759, 1789, 1589, 3624, 3627, 3625, 3628, 3629, 3647, 3645, 3648, 3649, 3675, 3678, 3679, 3658, 3659, 3689, 3247, 3245, 3248, 3249, 3275, 3278, 3279, 3258, 3259, 3289, 3475, 3478, 3479, 3458, 3459, 3489, 3758, 3759, 3789, 3589, 6247, 6245, 6248, 6249, 6275, 6278, 6279, 6258, 6259, 6289, 6475, 6478, 6479, 6458, 6459, 6489, 6758, 6759, 6789, 6589, 2475, 2478, 2479, 2458, 2459, 2489, 2758, 2759, 2789, 2589, 4758, 4759, 4789, 4589, 7589};
    // 斜跳方式所有組合
    private int[] combinations_B = {1367, 1364, 1368, 1362, 1365, 1369, 1374, 1378, 1372, 1375, 1379, 1348, 1342, 1345, 1349, 1382, 1385, 1389, 1325, 1329, 1359, 1674, 1678, 1672, 1675, 1679, 1648, 1642, 1645, 1649, 1682, 1685, 1689, 1625, 1629, 1659, 1748, 1742, 1745, 1749, 1782, 1785, 1789, 1725, 1729, 1759, 1482, 1485, 1489, 1425, 1429, 1459, 1825, 1829, 1859, 1259, 3674, 3678, 3672, 3675, 3679, 3648, 3642, 3645, 3649, 3682, 3685, 3689, 3625, 3629, 3659, 3748, 3742, 3745, 3749, 3782, 3785, 3789, 3725, 3729, 3759, 3482, 3485, 3489, 3425, 3429, 3459, 3825, 3829, 3859, 3259, 6748, 6742, 6745, 6749, 6782, 6785, 6789, 6725, 6729, 6759, 6482, 6485, 6489, 6425, 6429, 6459, 6825, 6829, 6859, 6259, 7482, 7485, 7489, 7425, 7429, 7459, 7825, 7829, 7859, 7259, 4825, 4829, 4859, 4259, 8259};
    // 按照數字小到大排序所有組合
    private int[] combinations_C = {1234, 1235, 1236, 1237, 1238, 1239, 1245, 1246, 1247, 1248, 1249, 1256, 1257, 1258, 1259, 1267, 1268, 1269, 1278, 1279, 1289, 1345, 1346, 1347, 1348, 1349, 1356, 1357, 1358, 1359, 1367, 1368, 1369, 1378, 1379, 1389, 1456, 1457, 1458, 1459, 1467, 1468, 1469, 1478, 1479, 1489, 1567, 1568, 1569, 1578, 1579, 1589, 1678, 1679, 1689, 1789, 2345, 2346, 2347, 2348, 2349, 2356, 2357, 2358, 2359, 2367, 2368, 2369, 2378, 2379, 2389, 2456, 2457, 2458, 2459, 2467, 2468, 2469, 2478, 2479, 2489, 2567, 2568, 2569, 2578, 2579, 2589, 2678, 2679, 2689, 2789, 3456, 3457, 3458, 3459, 3467, 3468, 3469, 3478, 3479, 3489, 3567, 3568, 3569, 3578, 3579, 3589, 3678, 3679, 3689, 3789, 4567, 4568, 4569, 4578, 4579, 4589, 4678, 4679, 4689, 4789, 5678, 5679, 5689, 5789, 6789};

    private int[] choose_combinations;

    private String selectedOption;

    private List<Boolean> dotVisible;

    private List<Integer> currentPosition = new ArrayList<>();

    private int cursor;

    private String message;

    private String imagePath = "";  // 用於顯示對應圖示

    public Bean() {
        // 預設為全部顯示
        dotVisible = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            dotVisible.add(false);  // 預設不顯示
        }
        //dotVisible.set(0,true);
    }

    private int currentIndex = 0;

    public void submit() {
        // 選擇跳法
        switch (selectedOption) {
            case "A":
                choose_combinations = combinations_A;
                break;
            case "B":
                choose_combinations = combinations_B;
                break;
            case "C":
                choose_combinations = combinations_C;
                break;
            default:
                choose_combinations = combinations_A;
                break;
        }
        currentIndex = 0;
        imagePath = "";     // 清空圖片
        message = "";
        updateDots();
    }

    public void next() {
        if (choose_combinations == null) {
            message = "請先選擇模式並提交";
            return;
        }

        if (currentIndex + 1 < choose_combinations.length) {
            currentIndex++;
            updateDots(); // 顯示下一組
            imagePath = "resources/imgs/wrong.png";  // 顯示錯誤圖片
        } else {
            message = "已無更多組合";
        }
    }

    public void correct() {
        currentIndex = 0;
        imagePath = "resources/imgs/correct.png";  // 顯示正確圖片
    }

    private void updateDots() {
        // 初始化 dotVisible 為 9 個 false（若尚未正確初始化）
        if (dotVisible == null || dotVisible.size() != 9) {
            dotVisible = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                dotVisible.add(false);
            }
        } else {
            for (int i = 0; i < 9; i++) {
                dotVisible.set(i, false);
            }
        }

        currentPosition.clear();
        if (choose_combinations == null || currentIndex >= choose_combinations.length) {
            return; // 安全性檢查
        }

        int value = choose_combinations[currentIndex];
        message = "當前組合: " + value;
        int cursor = 1000;
        while (cursor > 0) {
            int pos = (value / cursor) % 10;
            currentPosition.add(pos);
            cursor /= 10;
        }

        for (int pos : currentPosition) {
            if (pos >= 1 && pos <= 9) {
                dotVisible.set(pos - 1, true);
            }
        }
    }

    public String getDotStyle(int index) {
        int[][] positions = {
            // 1、2
            {15, 20}, {115, 20},
            // 3、4、5
            {15, 80}, {115, 80}, {215, 80},
            // 6、7、8、9
            {15, 140}, {115, 140}, {215, 140}, {320, 140}
        };
        if (index >= 0 && index < positions.length) {
            return String.format("position: absolute; left: %dpx; top: %dpx; width: 30px; height: 30px; background-color: rgb(0, 136, 255); border-radius: 50%%;", positions[index][0], positions[index][1]);
        } else {
            return "";
        }
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public List<Boolean> getDotVisible() {
        return dotVisible;
    }

    public void setDotVisible(List<Boolean> dotVisible) {
        this.dotVisible = dotVisible;
    }

    public String getMessage() {
        return message;
    }

    public String getImagePath() {
        return imagePath;
    }
}
