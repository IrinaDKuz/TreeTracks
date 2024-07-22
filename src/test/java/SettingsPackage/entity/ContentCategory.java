package SettingsPackage.entity;

import static Helper.ActionsClass.getCurrentDate;
import static Helper.Adverts.*;
import static Helper.Settings.*;

public class ContentCategory {

    CategoryLang engCategoryLang;
    CategoryLang rusCategoryLang;
    CategoryLang generalCategoryLang;

    public static class CategoryLang {
        Integer categoryId;
        String categoryLang;
        String categoryTitle;
        Integer categoryParentId;

        public CategoryLang(String lang, String title) {
            this.categoryLang = lang;
            this.categoryTitle = lang + "_" + title;
        }

        public CategoryLang() {
        }


        public String getCategoryLang() {
            return categoryLang;
        }

        public void setCategoryLang(String categoryLang) {
            this.categoryLang = categoryLang;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }

        public String getCategoryTitle() {
            return categoryTitle;
        }

        public void setCategoryTitle(String categoryTitle) {
            this.categoryTitle = categoryTitle;
        }

        public Integer getCategoryParentId() {
            return categoryParentId;
        }

        public void setCategoryParentId(Integer categoryParentId) {
            this.categoryParentId = categoryParentId;
        }

    }

    public ContentCategory() {
    }

    public void fillContentCategoryWithRandomData() {
        String title = generateName(1, CATEGORIES_WORDS);
        this.engCategoryLang = new CategoryLang("eng", title);
        this.rusCategoryLang = new CategoryLang("rus", title);
        this.generalCategoryLang = new CategoryLang("general", title);
    }


    public CategoryLang getEngCategoryLang() {
        return engCategoryLang;
    }

    public void setEngCategoryLang(CategoryLang engCategoryLang) {
        this.engCategoryLang = engCategoryLang;
    }

    public CategoryLang getRusCategoryLang() {
        return rusCategoryLang;
    }

    public void setRusCategoryLang(CategoryLang rusCategoryLang) {
        this.rusCategoryLang = rusCategoryLang;
    }

    public CategoryLang getGeneralCategoryLang() {
        return generalCategoryLang;
    }

    public void setGeneralCategoryLang(CategoryLang generalCategoryLang) {
        this.generalCategoryLang = generalCategoryLang;
    }
}