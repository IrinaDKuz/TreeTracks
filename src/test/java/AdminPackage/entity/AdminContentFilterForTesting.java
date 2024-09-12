package AdminPackage.entity;

import java.util.List;

public class AdminContentFilterForTesting {
    Boolean isInclude;
    String filterName;
    List<String> filterValue;
    List<Integer> expectedIds;

    public AdminContentFilterForTesting(boolean isInclude, String filterName, List<String> filterValue, List<Integer> expectedIds) {
        this.isInclude = isInclude;
        this.filterName = filterName;
        this.filterValue = filterValue;
        this.expectedIds = expectedIds;
    }

    public Boolean getInclude() {
        return isInclude;
    }

    public void setInclude(Boolean include) {
        isInclude = include;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public List<String> getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(List<String> filterValue) {
        this.filterValue = filterValue;
    }

    public List<Integer> getExpectedIds() {
        return expectedIds;
    }

    public void setExpectedIds(List<Integer> expectedIds) {
        this.expectedIds = expectedIds;
    }


}