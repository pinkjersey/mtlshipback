package com.ipmus;

import jetbrains.exodus.ArrayByteIterable;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Environments;
import jetbrains.exodus.env.Store;
import jetbrains.exodus.env.StoreConfig;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static jetbrains.exodus.bindings.StringBinding.stringToEntry;
import static jetbrains.exodus.bindings.StringBinding.entryToString;

/**
 * Created by mozturk on 7/26/2017.
 */

@SuppressWarnings("serial")
@WebServlet(name = "helloworld", value = "/banana" )
public class BananaServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final Environment env = Environments.newInstance("/home/mozturk/.myData");
        final Store store = env.computeInTransaction(txn -> env.openStore("Items", StoreConfig.WITHOUT_DUPLICATES, txn));

        // increment index value if exists, otherwise initialize with 0
        env.executeInExclusiveTransaction((txn) -> {
            ByteIterable indexEntry = store.get(txn, stringToEntry("ItemIndex"));
            if (indexEntry == null) {
                store.put(txn, stringToEntry("ItemIndex"),
                        stringToEntry("0"));
            } else {
                int indexVal = Integer.parseInt(entryToString(indexEntry))+1;
                store.put(txn, stringToEntry("ItemIndex"),
                        stringToEntry(Integer.toString(indexVal)));
            }
        });

        String indexStr = env.computeInTransaction(txn -> {
            ByteIterable indexEntry = store.get(txn, stringToEntry("ItemIndex"));
            return entryToString(indexEntry);
        });

        PrintWriter out = resp.getWriter();
        out.println(indexStr);

        env.close();

    }
}
