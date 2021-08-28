package com.ankur.securenotes.shared

class MemStore {

  /**
   * Stores the context and associated KeyValueStore.
   */
  private var store: ContextualStore? = null
    get() {
      if (field == null) {
        field = ContextualStore()
      }

      return field
    }

  /**
   * Save an object in the store.
   *
   * @param context The context against which (key, item) pair will be saved.
   * @param key The key that will be used to reference the item.
   * @param item The item that needs to be saved.
   */
  fun save(context: String, key: String, item: Any) {
    val mapForContext = store!![context]
    if (mapForContext == null) {
      store!![context] = HashMap<String, Any>()
    }

    store!![context]!![key] = item
  }

  /**
   * Retrieve all (key, item) pairs against given context.
   * After retrieval the store will be cleared of the retrieved items.
   *
   * @param context The context against which items will be retrieved.
   */
  fun retrieve(context: String): HashMap<String, Any>? {
    return store!!.remove(context)
  }

  /**
   * Retrieve an item pair from the store.
   * After retrieval the store will be cleared of the retrieved item.
   *
   * @param context The context against which item will be retrieved.
   * @param key The key for which the item will be retrieved.
   */
  fun retrieve(context: String, key: String): Any? {
    val value = store!![context]?.remove(key)
    if (store!![context]?.keys?.count() == 0) {
      store!!.remove(context)
    }

    return value
  }

  /**
   * Remove all (key, item) pairs for a given context.
   *
   * @param context The context which will be cleared.
   */
  fun remove(context: String) {
    store!!.remove(context)
  }

  /**
   * Retrieve an item from the store.
   *
   * @param context The context against which item will be removed.
   * @param key The key for which the item will be removed.
   */
  fun remove(context: String, key: String) {
    store!![context]?.remove(key)
    if (store!![context]?.keys?.count() == 0) {
      store!!.remove(context)
    }
  }

  /**
   * Remove everything from the store.
   */
  fun removeAll() {
    for (key in store!!.keys) {
      store!!.remove(key)
    }
  }

  /**
   * Get an item pair from the store.
   * This will not result in removal of the item from the store.
   *
   * @param context The context against which item will be retrieved.
   * @param key The key for which the item will be retrieved.
   */
  fun peek(context: String, key: String): Any? {
    return store!![context]?.get(key)
  }
}

private typealias ContextualStore = HashMap<String, KeyItemStore>
private typealias KeyItemStore = HashMap<String, Any>


