package com.mfizz.kyoto.demo;

/*
 * #%L
 * mfz-jne-kyotocabinet
 * %%
 * Copyright (C) 2012 - 2014 mfizz
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import kyotocabinet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author joelauer
 */
public class KyotoMain {

    private static final Logger logger = LoggerFactory.getLogger(KyotoMain.class);

    static public void main(String[] args) throws Exception {
        String dbname = "target/test.kch";
        if (args.length == 1) {
            dbname = args[0];
        }

        // create the object
        DB db = new DB();

        // open the database
        if (!db.open(dbname, DB.OWRITER | DB.OCREATE)) {
            System.err.println("open error: " + db.error());
        }

        // store records
        if (!db.set("foo", "hop")
                || !db.set("bar", "step")
                || !db.set("baz", "jump")) {
            System.err.println("set error: " + db.error());
        }

        // retrieve records
        String value = db.get("foo");
        if (value != null) {
            System.out.println(value);
        } else {
            System.err.println("set error: " + db.error());
        }

        // traverse records
        Cursor cur = db.cursor();
        cur.jump();
        String[] rec;
        while ((rec = cur.get_str(true)) != null) {
            System.out.println(rec[0] + ":" + rec[1]);
        }
        cur.disable();

        // close the database
        if (!db.close()) {
            System.err.println("close error: " + db.error());
        }

    }
}