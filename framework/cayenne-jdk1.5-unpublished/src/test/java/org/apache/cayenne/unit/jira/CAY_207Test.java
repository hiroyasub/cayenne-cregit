begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|unit
operator|.
name|jira
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|DataObjectUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DataContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DataDomain
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DataNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|types
operator|.
name|ExtendedTypeMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|exp
operator|.
name|Expression
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DataMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|ObjAttribute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|ObjEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|SQLTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|testdo
operator|.
name|inherit
operator|.
name|Manager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|unit
operator|.
name|PeopleCase
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|CAY_207Test
extends|extends
name|PeopleCase
block|{
specifier|protected
name|DataMap
name|testMap
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteTestData
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testCAY_207Super1
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testCAY_207"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|prepare
argument_list|()
expr_stmt|;
try|try
block|{
comment|// M1
comment|//            Manager o1 = DataObjectUtils.objectForPK(context, Manager.class, 1);
comment|//            assertTrue(o1 instanceof CAY_207Manager1);
comment|//
comment|//            Object p1 = o1.readProperty("clientContactType");
comment|//            assertNotNull(p1);
comment|//
comment|//            assertTrue(
comment|//                    "Invalid property class: " + p1.getClass().getName(),
comment|//                    p1 instanceof CAY_207String1);
block|}
finally|finally
block|{
name|cleanup
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testCAY_207Super2
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testCAY_207"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|prepare
argument_list|()
expr_stmt|;
try|try
block|{
comment|//            Manager o2 = DataObjectUtils.objectForPK(context, Manager.class, 2);
comment|//            assertTrue(o2 instanceof CAY_207Manager2);
comment|//
comment|//            Object p2 = o2.readProperty("clientContactType");
comment|//            assertNotNull(p2);
comment|//            assertTrue(
comment|//                    "Invalid property class: " + p2.getClass().getName(),
comment|//                    p2 instanceof CAY_207String2);
block|}
finally|finally
block|{
name|cleanup
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testCAY_207Subclass1
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testCAY_207"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|prepare
argument_list|()
expr_stmt|;
try|try
block|{
comment|// M1
comment|//            Manager o1 = DataObjectUtils.objectForPK(context, CAY_207Manager1.class, 1);
comment|//            assertTrue(o1 instanceof CAY_207Manager1);
comment|//
comment|//            Object p1 = o1.readProperty("clientContactType");
comment|//            assertNotNull(p1);
comment|//
comment|//            assertTrue(
comment|//                    "Invalid property class: " + p1.getClass().getName(),
comment|//                    p1 instanceof CAY_207String1);
block|}
finally|finally
block|{
name|cleanup
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testCAY_207Subclass2
parameter_list|()
throws|throws
name|Exception
block|{
name|createTestData
argument_list|(
literal|"testCAY_207"
argument_list|)
expr_stmt|;
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|prepare
argument_list|()
expr_stmt|;
try|try
block|{
comment|//            Manager o2 = DataObjectUtils.objectForPK(context, CAY_207Manager2.class, 2);
comment|//            assertTrue(o2 instanceof CAY_207Manager2);
comment|//
comment|//            Object p2 = o2.readProperty("clientContactType");
comment|//            assertNotNull(p2);
comment|//
comment|//            assertTrue(
comment|//                    "Invalid property class: " + p2.getClass().getName(),
comment|//                    p2 instanceof CAY_207String2);
block|}
finally|finally
block|{
name|cleanup
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testCAY_207Save
parameter_list|()
throws|throws
name|Exception
block|{
name|DataContext
name|context
init|=
name|createDataContext
argument_list|()
decl_stmt|;
name|prepare
argument_list|()
expr_stmt|;
try|try
block|{
comment|//            CAY_207Manager2 o2 = context.newObject(CAY_207Manager2.class);
comment|//            o2.setPersonType("M2");
comment|//            o2.setName("aaaa");
comment|//            o2.setClientContactType(new CAY_207String1("T1:AAAAA"));
comment|//
comment|//            // should succeed...
comment|//            context.commitChanges();
comment|//
comment|//            int pk = DataObjectUtils.intPKForObject(o2);
comment|//            String query = "SELECT #result('CLIENT_CONTACT_TYPE' 'String' 'CLIENT_CONTACT_TYPE') "
comment|//                    + "FROM PERSON WHERE PERSON_ID = "
comment|//                    + pk;
comment|//            SQLTemplate template = new SQLTemplate(CAY_207Manager2.class, query);
comment|//            template.setFetchingDataRows(true);
comment|//            List rows = context.performQuery(template);
comment|//            assertEquals(1, rows.size());
comment|//
comment|//            Map map = (Map) rows.get(0);
comment|//            assertEquals("T1:AAAAA", map.get("CLIENT_CONTACT_TYPE"));
block|}
finally|finally
block|{
name|cleanup
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|prepare
parameter_list|()
block|{
name|prepareDataMap
argument_list|()
expr_stmt|;
name|DataDomain
name|domain
init|=
name|getDomain
argument_list|()
decl_stmt|;
name|DataNode
name|node
init|=
name|domain
operator|.
name|lookupDataNode
argument_list|(
name|domain
operator|.
name|getMap
argument_list|(
literal|"people"
argument_list|)
argument_list|)
decl_stmt|;
name|domain
operator|.
name|removeDataNode
argument_list|(
name|node
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|node
operator|.
name|addDataMap
argument_list|(
name|testMap
argument_list|)
expr_stmt|;
name|domain
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|ExtendedTypeMap
name|map
init|=
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
decl_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|CAY_207StringType1
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|CAY_207StringType2
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|cleanup
parameter_list|(
name|DataContext
name|context
parameter_list|)
block|{
name|DataDomain
name|domain
init|=
name|getDomain
argument_list|()
decl_stmt|;
name|domain
operator|.
name|removeMap
argument_list|(
name|testMap
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|DataNode
name|node
init|=
name|domain
operator|.
name|lookupDataNode
argument_list|(
name|domain
operator|.
name|getMap
argument_list|(
literal|"people"
argument_list|)
argument_list|)
decl_stmt|;
name|ExtendedTypeMap
name|map
init|=
name|node
operator|.
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
decl_stmt|;
name|map
operator|.
name|unregisterType
argument_list|(
name|CAY_207String1
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|unregisterType
argument_list|(
name|CAY_207String2
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Overrides super implementation to add a few extra entities to this DataContext      * without affecting others.      */
specifier|protected
name|void
name|prepareDataMap
parameter_list|()
block|{
if|if
condition|(
name|testMap
operator|==
literal|null
condition|)
block|{
name|DataDomain
name|domain
init|=
name|getDomain
argument_list|()
decl_stmt|;
name|ObjEntity
name|manager
init|=
name|domain
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupObjEntity
argument_list|(
name|Manager
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjEntity
name|m1
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"Manager1"
argument_list|)
decl_stmt|;
name|m1
operator|.
name|setSuperEntityName
argument_list|(
name|manager
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|m1
operator|.
name|setDeclaredQualifier
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
literal|"personType = \"M1\""
argument_list|)
argument_list|)
expr_stmt|;
name|m1
operator|.
name|setClassName
argument_list|(
name|CAY_207Manager1
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ObjAttribute
name|ma1
init|=
operator|new
name|ObjAttribute
argument_list|(
literal|"clientContactType"
argument_list|)
decl_stmt|;
name|ma1
operator|.
name|setDbAttributePath
argument_list|(
literal|"CLIENT_CONTACT_TYPE"
argument_list|)
expr_stmt|;
name|ma1
operator|.
name|setType
argument_list|(
name|CAY_207String1
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ma1
operator|.
name|setEntity
argument_list|(
name|m1
argument_list|)
expr_stmt|;
name|m1
operator|.
name|addAttribute
argument_list|(
name|ma1
argument_list|)
expr_stmt|;
name|ObjEntity
name|m2
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"Manager2"
argument_list|)
decl_stmt|;
name|m2
operator|.
name|setSuperEntityName
argument_list|(
name|manager
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|m2
operator|.
name|setDeclaredQualifier
argument_list|(
name|Expression
operator|.
name|fromString
argument_list|(
literal|"personType = \"M2\""
argument_list|)
argument_list|)
expr_stmt|;
name|m2
operator|.
name|setClassName
argument_list|(
name|CAY_207Manager2
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ObjAttribute
name|ma2
init|=
operator|new
name|ObjAttribute
argument_list|(
literal|"clientContactType"
argument_list|)
decl_stmt|;
name|ma2
operator|.
name|setDbAttributePath
argument_list|(
literal|"CLIENT_CONTACT_TYPE"
argument_list|)
expr_stmt|;
name|ma2
operator|.
name|setType
argument_list|(
name|CAY_207String2
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ma2
operator|.
name|setEntity
argument_list|(
name|m2
argument_list|)
expr_stmt|;
name|m2
operator|.
name|addAttribute
argument_list|(
name|ma2
argument_list|)
expr_stmt|;
name|testMap
operator|=
operator|new
name|DataMap
argument_list|(
literal|"CAY-207"
argument_list|)
expr_stmt|;
name|testMap
operator|.
name|addObjEntity
argument_list|(
name|m1
argument_list|)
expr_stmt|;
name|testMap
operator|.
name|addObjEntity
argument_list|(
name|m2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

