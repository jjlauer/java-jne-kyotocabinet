package com.mfizz.kyoto;

import kyotocabinet.Cursor;
import kyotocabinet.DB;
import org.junit.Assert;
import org.junit.Test;

/*
 * Copyright 2014 mfizz.
 *
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
 */

/**
 *
 * @author joelauer
 */
public class LibraryTest {
    
    @Test
    public void simpleUse() throws Exception {
        // create the object
        DB db = new DB();

        // open the tree database
        Assert.assertTrue(db.open("target/simpleuse.kct", DB.OWRITER | DB.OCREATE));

        // store records
        Assert.assertTrue(db.set("foo", "hop"));
        Assert.assertTrue(db.set("bar", "step"));
        Assert.assertTrue(db.set("baz", "jump"));

        // retrieve records
        Assert.assertEquals("hop", db.get("foo"));
        Assert.assertEquals("step", db.get("bar"));
        Assert.assertEquals("jump", db.get("baz"));
        
        // this should not exist
        Assert.assertNull(db.get("notexist"));
        
        // delete the middle record we inserted
        db.remove("bar");
        
        Assert.assertNull(db.get("bar"));
        
        // traverse records with cursor (should be in alphabetical order for tree-db)
        /**
        Cursor cur = db.cursor();
        cur.jump();
        String[] rec;
        int i = 0;
        while ((rec = cur.get_str(true)) != null) {
            String key = cur.get_key_str(false);
            String value = cur.get_value_str(true);
            
            if (i == 0) {
                Assert.assertEquals("foo", key);
                Assert.assertEquals("hop", value);
            }
            
            i++;
        }
        cur.disable();
        */
        
        // close the database
        Assert.assertTrue(db.close());
        
        // should be persistent -- open it again
        // open the tree database
        Assert.assertTrue(db.open("target/simpleuse.kct", DB.OREADER));
        
        // only foo and baz should exist
        Assert.assertEquals("hop", db.get("foo"));
        Assert.assertEquals("jump", db.get("baz"));
        Assert.assertNull(db.get("bar"));
        
        // close the database
        Assert.assertTrue(db.close());
    }
    
    
    /**
    @Test
    public void libz() throws Exception {
        // create the object
        DB db = new DB();
        
        // open the tree database
        Assert.assertTrue(db.open("target/libz.kch#log=kc.log#logkinds=debug#zcomp=lzo", DB.OWRITER | DB.OCREATE));
        
        Assert.assertTrue(db.set("a", "aaaa"));
        Assert.assertTrue(db.set("b", "bbbb"));
        Assert.assertTrue(db.set("c", "cccc"));
    }
    */
    
}
