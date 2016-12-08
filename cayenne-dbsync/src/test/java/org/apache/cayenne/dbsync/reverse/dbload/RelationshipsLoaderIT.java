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
name|dbsync
operator|.
name|reverse
operator|.
name|dbload
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|DatabaseMetaData
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|dbsync
operator|.
name|naming
operator|.
name|DefaultObjectNameGenerator
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
name|DbRelationship
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
name|assertFalse
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
name|assertNotNull
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|RelationshipsLoaderIT
extends|extends
name|BaseLoaderIT
block|{
annotation|@
name|Test
specifier|public
name|void
name|testRelationshipLoad
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|supportsFK
init|=
name|accessStackAdapter
operator|.
name|supportsFKConstraints
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|supportsFK
condition|)
block|{
return|return;
block|}
name|DatabaseMetaData
name|metaData
init|=
name|connection
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
name|DbLoaderDelegate
name|delegate
init|=
operator|new
name|DefaultDbLoaderDelegate
argument_list|()
decl_stmt|;
comment|// We need all data to check relationships, so simply load it all
name|EntityLoader
name|entityLoader
init|=
operator|new
name|EntityLoader
argument_list|(
name|adapter
argument_list|,
name|EMPTY_CONFIG
argument_list|,
name|delegate
argument_list|)
decl_stmt|;
name|AttributeLoader
name|attributeLoader
init|=
operator|new
name|AttributeLoader
argument_list|(
name|adapter
argument_list|,
name|EMPTY_CONFIG
argument_list|,
name|delegate
argument_list|)
decl_stmt|;
name|PrimaryKeyLoader
name|primaryKeyLoader
init|=
operator|new
name|PrimaryKeyLoader
argument_list|(
name|EMPTY_CONFIG
argument_list|,
name|delegate
argument_list|)
decl_stmt|;
name|ExportedKeyLoader
name|exportedKeyLoader
init|=
operator|new
name|ExportedKeyLoader
argument_list|(
name|EMPTY_CONFIG
argument_list|,
name|delegate
argument_list|)
decl_stmt|;
name|entityLoader
operator|.
name|load
argument_list|(
name|metaData
argument_list|,
name|store
argument_list|)
expr_stmt|;
name|attributeLoader
operator|.
name|load
argument_list|(
name|metaData
argument_list|,
name|store
argument_list|)
expr_stmt|;
name|primaryKeyLoader
operator|.
name|load
argument_list|(
name|metaData
argument_list|,
name|store
argument_list|)
expr_stmt|;
name|exportedKeyLoader
operator|.
name|load
argument_list|(
name|metaData
argument_list|,
name|store
argument_list|)
expr_stmt|;
comment|// *** TESTING THIS ***
name|RelationshipLoader
name|relationshipLoader
init|=
operator|new
name|RelationshipLoader
argument_list|(
name|EMPTY_CONFIG
argument_list|,
name|delegate
argument_list|,
operator|new
name|DefaultObjectNameGenerator
argument_list|()
argument_list|)
decl_stmt|;
name|relationshipLoader
operator|.
name|load
argument_list|(
name|metaData
argument_list|,
name|store
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|DbRelationship
argument_list|>
name|rels
init|=
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
operator|.
name|getRelationships
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|rels
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|rels
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
comment|// test one-to-one
name|rels
operator|=
name|getDbEntity
argument_list|(
literal|"PAINTING"
argument_list|)
operator|.
name|getRelationships
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|rels
argument_list|)
expr_stmt|;
comment|// find relationship to PAINTING_INFO
name|DbRelationship
name|oneToOne
init|=
literal|null
decl_stmt|;
for|for
control|(
name|DbRelationship
name|rel
range|:
name|rels
control|)
block|{
if|if
condition|(
literal|"PAINTING_INFO"
operator|.
name|equalsIgnoreCase
argument_list|(
name|rel
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
condition|)
block|{
name|oneToOne
operator|=
name|rel
expr_stmt|;
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
literal|"No relationship to PAINTING_INFO"
argument_list|,
name|oneToOne
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Relationship to PAINTING_INFO must be to-one"
argument_list|,
name|oneToOne
operator|.
name|isToMany
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Relationship to PAINTING_INFO must be to-one"
argument_list|,
name|oneToOne
operator|.
name|isToDependentPK
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

