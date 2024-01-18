package ua.mei.spwp.client.screens.or.components;

import io.wispforest.owo.ui.container.*;
import io.wispforest.owo.ui.core.*;
import org.jetbrains.annotations.*;

import java.util.function.*;

public class FakeGrid extends GridLayout {
    private int currentRow = 0;
    private int currentColumn = 0;

    public FakeGrid(int rows, int columns) {
        super(Sizing.fixed(columns * 18), Sizing.fixed(rows * 18), rows, columns);
    }

    public FakeGrid child(Component child) {
        this.child(child, currentRow, currentColumn);

        this.currentColumn++;

        if (currentColumn % this.columns == 0) {
            this.currentColumn = 0;
            this.currentRow++;
        }

        return this;
    }
}
