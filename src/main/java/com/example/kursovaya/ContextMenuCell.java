package com.example.kursovaya;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ContextMenuCell {

    private ContextMenu menu;

    public ContextMenuCell(GridPane gridPane, int RowIndex){
        menu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Удалить");
        deleteItem.setOnAction(actionEvent -> {
            gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == RowIndex);
            for(Node child : gridPane.getChildren()){
                Integer nodeRowIndex = GridPane.getRowIndex(child);
                if (nodeRowIndex != null && nodeRowIndex > RowIndex){
                    GridPane.setRowIndex(child, nodeRowIndex-1);
                }
            }
            gridPane.getRowConstraints().remove(RowIndex);
        });

        menu.getItems().addAll(deleteItem);
    }

    public ContextMenu getMenu(){
        return menu;
    }
}
