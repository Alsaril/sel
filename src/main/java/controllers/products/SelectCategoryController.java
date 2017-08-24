package controllers.products;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import models.Node;
import models.treeView.SubcategoryItem;
import utils.Dialogs;

import java.util.List;

/**
 * Created by andrey on 16.08.17.
 */
public class SelectCategoryController {
    private Integer categoryId;
    private Integer subcategoryId;
    private List<Node> nodeList;
    private boolean ok;

    @FXML
    private TreeView<SubcategoryItem> categoryTreeView;


//    public void setCategoryAndSubcategory(List<Category> categories, List<Subcategory> subcategories){
//        categoryList = categories;
//        subcategoryList = subcategories;
//        categoryTreeView.setRoot(Data.categoryTreeViewRootItem(categoryList, subcategoryList));
//    }

    public void handleSelect(ActionEvent actionEvent){
        if (categoryTreeView.getSelectionModel().getSelectedItem().getValue()!=null) {
            SubcategoryItem subcategoryItem = categoryTreeView.getSelectionModel().getSelectedItem().getValue();
            if (subcategoryItem.getTrue_subcategory()) {
                subcategoryId = subcategoryItem.getId();
                //categoryId = Data.getSubcategoryById(subcategoryId, subcategoryList).getCategory();
            } else {
                categoryId = subcategoryItem.getId();
                subcategoryId = null;
            }
            ok = true;
            close();
        }else{
            Dialogs.showDialog("Ничего не выбрано!");
        }
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public Integer getSubcategoryId() {
        return subcategoryId;
    }

    public boolean isOk(){
        return ok;
    }

    private void close(){
        Stage stage = (Stage) categoryTreeView.getScene().getWindow();
        stage.close();
    }
}
