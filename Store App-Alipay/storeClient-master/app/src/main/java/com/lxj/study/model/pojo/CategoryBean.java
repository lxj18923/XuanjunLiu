package com.lxj.study.model.pojo;

public class CategoryBean {

        /**
         * id : 100026
         * parentId : 100005
         * name : LXJ
         * status : true
         * sortOrder :
         */

        private int id;
        private int parentId;
        private String name;
        private boolean status;
        private String sortOrder;
        private String createTime;
        private String updateTime;


        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

    public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getSortOrder() {
            return sortOrder;
        }

        public void setSortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
}
