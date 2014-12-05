package net.mostlyoriginal.paco;

import java.util.List;

/**
* Created by Paco on 09/11/2014.
* See LICENSE.md
*/
public interface IKnownMove {
    public List<Integer> getInputSequence();

    public String getMoveName();

    public int getLeniencyFrames();

    public int getMaxInputErrors();

    public int getFramesInSecond();
}
