begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|Inject
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
name|inheritance
operator|.
name|BaseEntity
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
name|inheritance
operator|.
name|RelatedEntity
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
name|inheritance
operator|.
name|SubEntity
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
name|di
operator|.
name|server
operator|.
name|CayenneProjects
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
name|di
operator|.
name|server
operator|.
name|ServerCase
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
name|di
operator|.
name|server
operator|.
name|UseServerRuntime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
annotation|@
name|UseServerRuntime
argument_list|(
name|CayenneProjects
operator|.
name|INHERITANCE_PROJECT
argument_list|)
specifier|public
class|class
name|EntityInheritanceIT
extends|extends
name|ServerCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
comment|/**      * Test for CAY-1008: Reverse relationships may not be correctly set if inheritance is      * used.      */
annotation|@
name|Test
specifier|public
name|void
name|testCAY1008
parameter_list|()
block|{
name|RelatedEntity
name|related
init|=
name|context
operator|.
name|newObject
argument_list|(
name|RelatedEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|BaseEntity
name|base
init|=
name|context
operator|.
name|newObject
argument_list|(
name|BaseEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|base
operator|.
name|setToRelatedEntity
argument_list|(
name|related
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|related
operator|.
name|getBaseEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|related
operator|.
name|getSubEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SubEntity
name|sub
init|=
name|context
operator|.
name|newObject
argument_list|(
name|SubEntity
operator|.
name|class
argument_list|)
decl_stmt|;
name|sub
operator|.
name|setToRelatedEntity
argument_list|(
name|related
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|related
operator|.
name|getBaseEntities
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: andrus 2008/03/28 - this fails...
comment|// assertEquals(1, related.getSubEntities().size());
block|}
comment|/**      * Test for CAY-1009: Bogus runtime relationships can mess up commit.      */
annotation|@
name|Test
specifier|public
name|void
name|testCAY1009
parameter_list|()
block|{
comment|// We should have only one relationship. DirectToSubEntity -> SubEntity.
comment|// this fails as a result of 'EntityResolver().applyObjectLayerDefaults()'
comment|// creating incorrect relationships
comment|// assertEquals(1, context
comment|// .getEntityResolver()
comment|// .getObjEntity("DirectToSubEntity")
comment|// .getRelationships()
comment|// .size());
comment|// We should still just have the one mapped relationship, but we in fact now have
comment|// two:
comment|// DirectToSubEntity -> BaseEntity and DirectToSubEntity -> SubEntity.
comment|// TODO: andrus 2008/03/28 - this fails...
comment|// assertEquals(1, context.getEntityResolver().getObjEntity("DirectToSubEntity")
comment|// .getRelationships().size());
comment|//
comment|// DirectToSubEntity direct = context.newObject(DirectToSubEntity.class);
comment|//
comment|// SubEntity sub = context.newObject(SubEntity.class);
comment|// sub.setToDirectToSubEntity(direct);
comment|//
comment|// assertEquals(1, direct.getSubEntities().size());
comment|//
comment|// context.deleteObject(sub);
comment|// assertEquals(0, direct.getSubEntities().size());
block|}
block|}
end_class

end_unit

