/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.agent.plugin.metrics.core.advice;

import org.apache.shardingsphere.agent.plugin.metrics.core.MetricsPool;
import org.apache.shardingsphere.agent.plugin.metrics.core.constant.MetricIds;
import org.apache.shardingsphere.agent.plugin.metrics.core.fixture.FixtureWrapper;
import org.apache.shardingsphere.infra.route.context.RouteContext;
import org.apache.shardingsphere.infra.route.context.RouteMapper;
import org.apache.shardingsphere.infra.route.context.RouteUnit;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public final class RouteResultCountAdviceTest extends MetricsAdviceBaseTest {
    
    @Test
    public void assertCountRouteResult() {
        RouteContext routeContext = new RouteContext();
        RouteMapper dataSourceMapper = new RouteMapper("logic_db", "ds_0");
        RouteMapper tableMapper = new RouteMapper("t_order", "t_order_0");
        routeContext.getRouteUnits().add(new RouteUnit(dataSourceMapper, Collections.singleton(tableMapper)));
        new RouteResultCountAdvice().afterMethod(new MockTargetAdviceObject(), mock(Method.class), new Object[]{}, routeContext);
        FixtureWrapper wrapper = (FixtureWrapper) MetricsPool.get(MetricIds.ROUTED_DATA_SOURCES).get();
        assertTrue(MetricsPool.get(MetricIds.ROUTED_DATA_SOURCES).isPresent());
        assertThat(wrapper.getFixtureValue(), is(1d));
        wrapper = (FixtureWrapper) MetricsPool.get(MetricIds.ROUTED_TABLES).get();
        assertTrue(MetricsPool.get(MetricIds.ROUTED_TABLES).isPresent());
        assertThat(wrapper.getFixtureValue(), is(1d));
    }
}
