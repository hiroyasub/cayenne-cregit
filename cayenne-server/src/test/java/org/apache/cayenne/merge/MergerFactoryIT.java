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
name|merge
package|;
end_package

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
name|assertNull
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
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
name|CayenneDataObject
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
name|map
operator|.
name|DbAttribute
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
name|DbEntity
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
name|DbJoin
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|MergerFactoryIT
extends|extends
name|MergeCase
block|{
annotation|@
name|Inject
specifier|private
name|DataContext
name|context
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testAddAndDropColumnToDb
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|dbEntity
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"PAINTING"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
comment|// create and add new column to model and db
name|DbAttribute
name|column
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"NEWCOL1"
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|dbEntity
argument_list|)
decl_stmt|;
name|column
operator|.
name|setMandatory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|column
operator|.
name|setMaxLength
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addAttribute
argument_list|(
name|column
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// try merge once more to check that is was merged
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// remove it from model and db
name|dbEntity
operator|.
name|removeAttribute
argument_list|(
name|column
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testChangeVarcharSizeToDb
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|dbEntity
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"PAINTING"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
comment|// create and add new column to model and db
name|DbAttribute
name|column
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"NEWCOL2"
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|dbEntity
argument_list|)
decl_stmt|;
name|column
operator|.
name|setMandatory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|column
operator|.
name|setMaxLength
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addAttribute
argument_list|(
name|column
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// check that is was merged
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// change size
name|column
operator|.
name|setMaxLength
argument_list|(
literal|20
argument_list|)
expr_stmt|;
comment|// merge to db
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// check that is was merged
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// clean up
name|dbEntity
operator|.
name|removeAttribute
argument_list|(
name|column
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleTokensToDb
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|dbEntity
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"PAINTING"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|DbAttribute
name|column1
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"NEWCOL3"
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|dbEntity
argument_list|)
decl_stmt|;
name|column1
operator|.
name|setMandatory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|column1
operator|.
name|setMaxLength
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addAttribute
argument_list|(
name|column1
argument_list|)
expr_stmt|;
name|DbAttribute
name|column2
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"NEWCOL4"
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|dbEntity
argument_list|)
decl_stmt|;
name|column2
operator|.
name|setMandatory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|column2
operator|.
name|setMaxLength
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addAttribute
argument_list|(
name|column2
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// check that is was merged
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// change size
name|column1
operator|.
name|setMaxLength
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|column2
operator|.
name|setMaxLength
argument_list|(
literal|30
argument_list|)
expr_stmt|;
comment|// merge to db
name|assertTokensAndExecute
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// check that is was merged
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// clean up
name|dbEntity
operator|.
name|removeAttribute
argument_list|(
name|column1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|removeAttribute
argument_list|(
name|column2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddTableToDb
parameter_list|()
throws|throws
name|Exception
block|{
name|dropTableIfPresent
argument_list|(
literal|"NEW_TABLE"
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|DbEntity
name|dbEntity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"NEW_TABLE"
argument_list|)
decl_stmt|;
name|DbAttribute
name|column1
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"ID"
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
name|dbEntity
argument_list|)
decl_stmt|;
name|column1
operator|.
name|setMandatory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|column1
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addAttribute
argument_list|(
name|column1
argument_list|)
expr_stmt|;
name|DbAttribute
name|column2
init|=
operator|new
name|DbAttribute
argument_list|(
literal|"NAME"
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|dbEntity
argument_list|)
decl_stmt|;
name|column2
operator|.
name|setMaxLength
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|column2
operator|.
name|setMandatory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addAttribute
argument_list|(
name|column2
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|ObjEntity
name|objEntity
init|=
operator|new
name|ObjEntity
argument_list|(
literal|"NewTable"
argument_list|)
decl_stmt|;
name|objEntity
operator|.
name|setDbEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|ObjAttribute
name|oatr1
init|=
operator|new
name|ObjAttribute
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|oatr1
operator|.
name|setDbAttributePath
argument_list|(
name|column2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|oatr1
operator|.
name|setType
argument_list|(
literal|"java.lang.String"
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|addAttribute
argument_list|(
name|oatr1
argument_list|)
expr_stmt|;
name|map
operator|.
name|addObjEntity
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|CayenneDataObject
name|dao
init|=
operator|(
name|CayenneDataObject
operator|)
name|context
operator|.
name|newObject
argument_list|(
name|objEntity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|dao
operator|.
name|writeProperty
argument_list|(
name|oatr1
operator|.
name|getName
argument_list|()
argument_list|,
literal|"test "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
comment|// clear up
name|map
operator|.
name|removeObjEntity
argument_list|(
name|objEntity
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeDbEntity
argument_list|(
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|refreshMappingCache
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getObjEntity
argument_list|(
name|objEntity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|map
operator|.
name|getDbEntity
argument_list|(
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|getDbEntities
argument_list|()
operator|.
name|contains
argument_list|(
name|dbEntity
argument_list|)
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddForeignKeyWithTable
parameter_list|()
throws|throws
name|Exception
block|{
name|dropTableIfPresent
argument_list|(
literal|"NEW_TABLE"
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|DbEntity
name|dbEntity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"NEW_TABLE"
argument_list|)
decl_stmt|;
name|attr
argument_list|(
name|dbEntity
argument_list|,
literal|"ID"
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|attr
argument_list|(
name|dbEntity
argument_list|,
literal|"NAME"
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
operator|.
name|setMaxLength
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|attr
argument_list|(
name|dbEntity
argument_list|,
literal|"ARTIST_ID"
argument_list|,
name|Types
operator|.
name|BIGINT
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|DbEntity
name|artistDbEntity
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artistDbEntity
argument_list|)
expr_stmt|;
comment|// relation from new_table to artist
name|DbRelationship
name|r1
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"toArtistR1"
argument_list|)
decl_stmt|;
name|r1
operator|.
name|setSourceEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setTargetEntity
argument_list|(
name|artistDbEntity
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setToMany
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|r1
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|r1
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addRelationship
argument_list|(
name|r1
argument_list|)
expr_stmt|;
comment|// relation from artist to new_table
name|DbRelationship
name|r2
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"toNewTableR2"
argument_list|)
decl_stmt|;
name|r2
operator|.
name|setSourceEntity
argument_list|(
name|artistDbEntity
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setTargetEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setToMany
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|r2
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|r2
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|artistDbEntity
operator|.
name|addRelationship
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// remove relationships
name|dbEntity
operator|.
name|removeRelationship
argument_list|(
name|r1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|artistDbEntity
operator|.
name|removeRelationship
argument_list|(
name|r2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|refreshMappingCache
argument_list|()
expr_stmt|;
comment|/*          * Db -Rel 'toArtistR1' - NEW_TABLE 1 -> 1 ARTIST" r2 =     * Db -Rel 'toNewTableR2' - ARTIST 1 -> * NEW_TABLE"          * */
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// clear up
comment|// map.removeObjEntity(objEntity.getName(), true);
name|map
operator|.
name|removeDbEntity
argument_list|(
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|refreshMappingCache
argument_list|()
expr_stmt|;
comment|// assertNull(map.getObjEntity(objEntity.getName()));
name|assertNull
argument_list|(
name|map
operator|.
name|getDbEntity
argument_list|(
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|getDbEntities
argument_list|()
operator|.
name|contains
argument_list|(
name|dbEntity
argument_list|)
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddForeignKeyAfterTable
parameter_list|()
throws|throws
name|Exception
block|{
name|dropTableIfPresent
argument_list|(
literal|"NEW_TABLE"
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|DbEntity
name|dbEntity
init|=
operator|new
name|DbEntity
argument_list|(
literal|"NEW_TABLE"
argument_list|)
decl_stmt|;
name|attr
argument_list|(
name|dbEntity
argument_list|,
literal|"ID"
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|attr
argument_list|(
name|dbEntity
argument_list|,
literal|"NAME"
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
operator|.
name|setMaxLength
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|attr
argument_list|(
name|dbEntity
argument_list|,
literal|"ARTIST_ID"
argument_list|,
name|Types
operator|.
name|BIGINT
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|DbEntity
name|artistDbEntity
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artistDbEntity
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// relation from new_table to artist
name|DbRelationship
name|r1
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"toArtistR1"
argument_list|)
decl_stmt|;
name|r1
operator|.
name|setSourceEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setTargetEntity
argument_list|(
name|artistDbEntity
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setToMany
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|r1
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|r1
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addRelationship
argument_list|(
name|r1
argument_list|)
expr_stmt|;
comment|// relation from artist to new_table
name|DbRelationship
name|r2
init|=
operator|new
name|DbRelationship
argument_list|(
literal|"toNewTableR2"
argument_list|)
decl_stmt|;
name|r2
operator|.
name|setSourceEntity
argument_list|(
name|artistDbEntity
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setTargetEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setToMany
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|r2
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|r2
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|artistDbEntity
operator|.
name|addRelationship
argument_list|(
name|r2
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// remove relationships
name|dbEntity
operator|.
name|removeRelationship
argument_list|(
name|r1
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|artistDbEntity
operator|.
name|removeRelationship
argument_list|(
name|r2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|refreshMappingCache
argument_list|()
expr_stmt|;
comment|/*         * Add Relationship ARTIST->NEW_TABLE To Model         * Drop Relationship NEW_TABLE->ARTIST To DB         * */
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// clear up
comment|// map.removeObjEntity(objEntity.getName(), true);
name|map
operator|.
name|removeDbEntity
argument_list|(
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|resolver
operator|.
name|refreshMappingCache
argument_list|()
expr_stmt|;
comment|// assertNull(map.getObjEntity(objEntity.getName()));
name|assertNull
argument_list|(
name|map
operator|.
name|getDbEntity
argument_list|(
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|map
operator|.
name|getDbEntities
argument_list|()
operator|.
name|contains
argument_list|(
name|dbEntity
argument_list|)
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertTokensAndExecute
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|DbAttribute
name|attr
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|,
name|String
name|name
parameter_list|,
name|int
name|type
parameter_list|,
name|boolean
name|mandatory
parameter_list|,
name|boolean
name|primaryKey
parameter_list|)
block|{
name|DbAttribute
name|column1
init|=
operator|new
name|DbAttribute
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|dbEntity
argument_list|)
decl_stmt|;
name|column1
operator|.
name|setMandatory
argument_list|(
name|mandatory
argument_list|)
expr_stmt|;
name|column1
operator|.
name|setPrimaryKey
argument_list|(
name|primaryKey
argument_list|)
expr_stmt|;
name|dbEntity
operator|.
name|addAttribute
argument_list|(
name|column1
argument_list|)
expr_stmt|;
return|return
name|column1
return|;
block|}
block|}
end_class

end_unit

