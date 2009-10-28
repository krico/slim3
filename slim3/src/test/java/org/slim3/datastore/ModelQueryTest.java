/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.slim3.datastore;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slim3.datastore.meta.BarMeta;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.LocalServiceTestCase;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public class ModelQueryTest extends LocalServiceTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void constructor() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        assertThat(query.modelMeta, is(sameInstance((ModelMeta) meta)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void filter() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        assertThat(query, is(sameInstance(query.filter(meta.myString
            .equal("aaa")))));
        assertThat(query.query.getFilterPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void filterForIllegalArgument() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        query.filter(new BarMeta().key.equal(null));
    }

    /**
     * @throws Exception
     */
    @Test
    public void sort() throws Exception {
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        assertThat(query.sort(meta.myString.asc), is(sameInstance(query)));
        assertThat(query.query.getSortPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asList() throws Exception {
        Datastore.put(new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        List<Hoge> list = query.asList();
        assertThat(list.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingle() throws Exception {
        Datastore.put(new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        Hoge hoge = query.asSingle();
        assertThat(hoge, is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asKeyList() throws Exception {
        Key key = Datastore.put(new Entity("Hoge"));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta);
        assertThat(query.asKeyList(), is(Arrays.asList(key)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void ancestor() throws Exception {
        Key key = Datastore.put(new Entity("Parent"));
        Datastore.put(new Entity("Hoge", key));
        ModelQuery<Hoge> query = new ModelQuery<Hoge>(meta, key);
        List<Hoge> list = query.asList();
        assertThat(list.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void min() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        Hoge hoge2 = new Hoge();
        hoge2.setMyInteger(2);
        Hoge hoge3 = new Hoge();
        Datastore.put(hoge, hoge2, hoge3);
        assertThat(Datastore.query(meta).min(meta.myInteger), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void max() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyInteger(1);
        Hoge hoge2 = new Hoge();
        hoge2.setMyInteger(2);
        Hoge hoge3 = new Hoge();
        Datastore.put(hoge, hoge2, hoge3);
        assertThat(Datastore.query(meta).max(meta.myInteger), is(2));
    }
}