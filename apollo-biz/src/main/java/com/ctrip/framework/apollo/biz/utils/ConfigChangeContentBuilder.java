package com.ctrip.framework.apollo.biz.utils;

import com.ctrip.framework.apollo.biz.entity.Item;
import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class ConfigChangeContentBuilder {

  private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

  /**
   * 创建 Item 集合
   */
  private List<Item> createItems = new LinkedList<>();

  /**
   * 更新 Item 集合
   */
  private List<ItemPair> updateItems = new LinkedList<>();

  /**
   * 删除 Item 集合
   */
  private List<Item> deleteItems = new LinkedList<>();


  public ConfigChangeContentBuilder createItem(Item item) {
    if (!StringUtils.isEmpty(item.getKey())){
      createItems.add(cloneItem(item));
    }
    return this;
  }

  public ConfigChangeContentBuilder updateItem(Item oldItem, Item newItem) {
    if (!oldItem.getValue().equals(newItem.getValue())){
      ItemPair itemPair = new ItemPair(cloneItem(oldItem), cloneItem(newItem));
      updateItems.add(itemPair);
    }
    return this;
  }

  public ConfigChangeContentBuilder deleteItem(Item item) {
    if (!StringUtils.isEmpty(item.getKey())) {
      deleteItems.add(cloneItem(item));
    }
    return this;
  }

  public boolean hasContent(){
    return !createItems.isEmpty() || !updateItems.isEmpty() || !deleteItems.isEmpty();
  }


//  构建 Item 变化的 JSON 字符串
  public String build() {
    //因为事务第一段提交并没有更新时间,所以build时统一更新
    Date now = new Date();

    for (Item item : createItems) {
      item.setDataChangeLastModifiedTime(now);
    }

    for (ItemPair item : updateItems) {
      item.newItem.setDataChangeLastModifiedTime(now);
    }

    for (Item item : deleteItems) {
      item.setDataChangeLastModifiedTime(now);
    }
    // JSON 格式化成字符串
    return gson.toJson(this);
  }

  static class ItemPair {

    Item oldItem;
    Item newItem;

    public ItemPair(Item oldItem, Item newItem) {
      this.oldItem = oldItem;
      this.newItem = newItem;
    }
  }

  Item cloneItem(Item source) {
    Item target = new Item();

    BeanUtils.copyProperties(source, target);

    return target;
  }

  public static ConfigChangeContentBuilder convertJsonString(String content) {
    return gson.fromJson(content, ConfigChangeContentBuilder.class);
  }

  public List<Item> getCreateItems() {
    return createItems;
  }

  public List<ItemPair> getUpdateItems() {
    return updateItems;
  }

  public List<Item> getDeleteItems() {
    return deleteItems;
  }
}
