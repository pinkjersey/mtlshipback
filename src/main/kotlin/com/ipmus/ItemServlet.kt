package com.ipmus

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ipmus.entities.Item
import jetbrains.exodus.bindings.StringBinding
import jetbrains.exodus.env.Store
import jetbrains.exodus.env.StoreConfig

import java.io.IOException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import jetbrains.exodus.entitystore.PersistentEntityStore
import jetbrains.exodus.entitystore.PersistentEntityStores
import javax.ws.rs.Path


/**
 * Created by mozturk on 7/26/2017.
 */
@WebServlet(name = "mtlshipping", value = "/itemzzzz")
class ItemServlet : HttpServlet() {
    @Path("items")
    @Throws(IOException::class)
    public override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        val out = resp.writer
        val mapper = jacksonObjectMapper()
        val entityStore = PersistentEntityStores.newInstance("/home/mozturk/.shipData")
        val items: List<Item> = entityStore.computeInReadonlyTransaction { txn ->
            txn.getAll("Item").map { Item(it) }
        }
        mapper.writeValue(out, items)

        /*val env = Environments.newInstance("/home/mozturk/.myData")

        val store = env.computeInTransaction { txn -> env.openStore("Items", StoreConfig.WITHOUT_DUPLICATES, txn) }

        // increment index value if exists, otherwise initialize with 0
        env.executeInExclusiveTransaction { txn ->
            val indexEntry = store.get(txn, StringBinding.stringToEntry("ItemIndex"))
            if (indexEntry == null) {
                store.put(txn, StringBinding.stringToEntry("ItemIndex"),
                        StringBinding.stringToEntry("0"))
            } else {
                val indexVal = Integer.parseInt(StringBinding.entryToString(indexEntry)) + 1
                store.put(txn, StringBinding.stringToEntry("ItemIndex"),
                        StringBinding.stringToEntry(Integer.toString(indexVal)))
            }
        }

        val indexStr = env.computeInTransaction<String> { txn ->
            val indexEntry = store.get(txn, StringBinding.stringToEntry("ItemIndex"))
            StringBinding.entryToString(indexEntry!!)
        }

        val out = resp.writer
        out.println(indexStr)

        env.close()*/

    }
}