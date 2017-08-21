package utils;

import javafx.scene.control.TreeItem;
import models.Category;
import models.Subcategory;
import models.treeView.SubcategoryItem;

import java.util.List;

/**
 * Created by andrey on 16.08.17.
 */
public class Data {
    public static TreeItem<SubcategoryItem> categoryTreeViewRootItem(List<Category> categories, List<Subcategory> subcategories) {
        SubcategoryItem catRoot = new SubcategoryItem("Все");
        catRoot.setTrue_subcategory(false);
        TreeItem<SubcategoryItem> rootItem = new TreeItem<SubcategoryItem>(catRoot);
        for (int i = 0; i < categories.size(); i++) {
            SubcategoryItem categoryItem = new SubcategoryItem(categories.get(i).getId(), categories.get(i).getName(), false);
            TreeItem<SubcategoryItem> item = new TreeItem<SubcategoryItem>(categoryItem);
            int id = categories.get(i).getId();
            for (int j = 0; j < subcategories.size(); j++) {
                if (subcategories.get(j).getCategory() == id) {
                    SubcategoryItem subcategoryItem = new SubcategoryItem(subcategories.get(j).getId(), subcategories.get(j).getName(), true);
                    subcategoryItem.setCategory(subcategories.get(j).getCategory());
                    TreeItem<SubcategoryItem> subitem = new TreeItem<SubcategoryItem>(subcategoryItem);
                    item.getChildren().add(subitem);
                }
            }
            rootItem.getChildren().add(item);
        }
        return rootItem;
    }

    public static Category getCategoryById(int id, List<Category> categoryList){
        Category category = null;
        if (categoryList!=null){
            for (int i = 0; i < categoryList.size(); i++) {
                if (categoryList.get(i).getId() == id){
                    category = categoryList.get(i);
                }
            }
        }
        return category;
    }

    public static Subcategory getSubcategoryById(int id, List<Subcategory> subcategoryList){
        Subcategory subcategory = null;
        if (subcategoryList!=null){
            for (int i = 0; i < subcategoryList.size(); i++) {
                if (subcategoryList.get(i).getId() == id){
                    subcategory = subcategoryList.get(i);
                }
            }
        }
        return subcategory;
    }
}
