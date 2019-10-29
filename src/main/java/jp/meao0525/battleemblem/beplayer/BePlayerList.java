package jp.meao0525.battleemblem.beplayer;

import java.util.ArrayList;
import java.util.List;

public class BePlayerList {
    private List<BePlayer> bePlayerList = new ArrayList<>();

    public BePlayerList() {
    }

    public void add(BePlayer bePlayer) {
        bePlayerList.add(bePlayer);
    }

    public List<BePlayer> getBePlayerList() {
        return bePlayerList;
    }
}
