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
name|access
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

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
name|org
operator|.
name|objectstyle
operator|.
name|ashwood
operator|.
name|dbutil
operator|.
name|Table
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
name|dba
operator|.
name|TypesMapping
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
name|apache
operator|.
name|cayenne
operator|.
name|unit
operator|.
name|CayenneCase
import|;
end_import

begin_class
specifier|public
class|class
name|DbLoaderTest
extends|extends
name|CayenneCase
block|{
specifier|protected
name|DbLoader
name|loader
decl_stmt|;
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
name|loader
operator|=
operator|new
name|DbLoader
argument_list|(
name|getConnection
argument_list|()
argument_list|,
name|getNode
argument_list|()
operator|.
name|getAdapter
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetTableTypes
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|List
name|tableTypes
init|=
name|loader
operator|.
name|getTableTypes
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|tableTypes
argument_list|)
expr_stmt|;
name|String
name|tableLabel
init|=
name|getNode
argument_list|()
operator|.
name|getAdapter
argument_list|()
operator|.
name|tableTypeForTable
argument_list|()
decl_stmt|;
if|if
condition|(
name|tableLabel
operator|!=
literal|null
condition|)
block|{
name|assertTrue
argument_list|(
literal|"Missing type for table '"
operator|+
name|tableLabel
operator|+
literal|"' - "
operator|+
name|tableTypes
argument_list|,
name|tableTypes
operator|.
name|contains
argument_list|(
name|tableLabel
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|viewLabel
init|=
name|getNode
argument_list|()
operator|.
name|getAdapter
argument_list|()
operator|.
name|tableTypeForView
argument_list|()
decl_stmt|;
if|if
condition|(
name|viewLabel
operator|!=
literal|null
condition|)
block|{
name|assertTrue
argument_list|(
literal|"Missing type for view '"
operator|+
name|viewLabel
operator|+
literal|"' - "
operator|+
name|tableTypes
argument_list|,
name|tableTypes
operator|.
name|contains
argument_list|(
name|viewLabel
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|loader
operator|.
name|getCon
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testGetTables
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|String
name|tableLabel
init|=
name|getNode
argument_list|()
operator|.
name|getAdapter
argument_list|()
operator|.
name|tableTypeForTable
argument_list|()
decl_stmt|;
name|List
name|tables
init|=
name|loader
operator|.
name|getTables
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"%"
argument_list|,
operator|new
name|String
index|[]
block|{
name|tableLabel
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|tables
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|tables
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|boolean
name|foundArtist
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Table
name|table
init|=
operator|(
name|Table
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"ARTIST"
operator|.
name|equalsIgnoreCase
argument_list|(
name|table
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|foundArtist
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|assertTrue
argument_list|(
literal|"'ARTIST' is missing from the table list: "
operator|+
name|tables
argument_list|,
name|foundArtist
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|loader
operator|.
name|getCon
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * DataMap loading is in one big test method, since breaking it in individual tests      * would require multiple reads of metatdata which is extremely slow on some RDBMS      * (Sybase).      */
specifier|public
name|void
name|testLoad
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|boolean
name|supportsUnique
init|=
name|getNode
argument_list|()
operator|.
name|getAdapter
argument_list|()
operator|.
name|supportsUniqueConstraints
argument_list|()
decl_stmt|;
name|boolean
name|supportsLobs
init|=
name|getAccessStackAdapter
argument_list|()
operator|.
name|supportsLobs
argument_list|()
decl_stmt|;
name|DataMap
name|map
init|=
operator|new
name|DataMap
argument_list|()
decl_stmt|;
name|String
name|tableLabel
init|=
name|getNode
argument_list|()
operator|.
name|getAdapter
argument_list|()
operator|.
name|tableTypeForTable
argument_list|()
decl_stmt|;
comment|// *** TESTING THIS ***
name|loader
operator|.
name|loadDbEntities
argument_list|(
name|map
argument_list|,
name|loader
operator|.
name|getTables
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|"%"
argument_list|,
operator|new
name|String
index|[]
block|{
name|tableLabel
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|assertDbEntities
argument_list|(
name|map
argument_list|)
expr_stmt|;
if|if
condition|(
name|supportsLobs
condition|)
block|{
name|assertLobDbEntities
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
comment|// *** TESTING THIS ***
name|loader
operator|.
name|loadDbRelationships
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|Collection
name|rels
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
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
name|rels
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// test one-to-one
name|rels
operator|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
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
name|Iterator
name|it
init|=
name|rels
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbRelationship
name|rel
init|=
operator|(
name|DbRelationship
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
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
comment|// test UNIQUE only if FK is supported...
if|if
condition|(
name|supportsUnique
condition|)
block|{
name|assertUniqueConstraintsInRelationships
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
comment|// *** TESTING THIS ***
name|loader
operator|.
name|loadObjEntities
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|ObjEntity
name|ae
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"Artist"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ae
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Artist"
argument_list|,
name|ae
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// assert primary key is not an attribute
name|assertNull
argument_list|(
name|ae
operator|.
name|getAttribute
argument_list|(
literal|"artistId"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|supportsLobs
condition|)
block|{
name|assertLobObjEntities
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
name|Collection
name|rels1
init|=
name|ae
operator|.
name|getRelationships
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|rels1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|rels1
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
comment|// now when the map is loaded, test
comment|// various things
comment|// selectively check how different types were processed
name|checkTypes
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|loader
operator|.
name|getCon
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|assertUniqueConstraintsInRelationships
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
comment|// unfortunately JDBC metadata doesn't provide info for UNIQUE constraints....
comment|// cant reengineer them...
comment|// find rel to TO_ONEFK1
comment|/*          * Iterator it = getDbEntity(map, "TO_ONEFK2").getRelationships().iterator();          * DbRelationship rel = (DbRelationship) it.next(); assertEquals("TO_ONEFK1",          * rel.getTargetEntityName()); assertFalse("UNIQUE constraint was ignored...",          * rel.isToMany());          */
block|}
specifier|private
name|void
name|assertDbEntities
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|DbEntity
name|dae
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"ARTIST"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Null 'ARTIST' entity, other DbEntities: "
operator|+
name|map
operator|.
name|getDbEntityMap
argument_list|()
argument_list|,
name|dae
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ARTIST"
argument_list|,
name|dae
operator|.
name|getName
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
name|DbAttribute
name|a
init|=
name|getDbAttribute
argument_list|(
name|dae
argument_list|,
literal|"ARTIST_ID"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|a
operator|.
name|isPrimaryKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertLobDbEntities
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|DbEntity
name|blobEnt
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"BLOB_TEST"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|blobEnt
argument_list|)
expr_stmt|;
name|DbAttribute
name|blobAttr
init|=
name|getDbAttribute
argument_list|(
name|blobEnt
argument_list|,
literal|"BLOB_COL"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|blobAttr
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|Types
operator|.
name|BLOB
argument_list|,
name|blobAttr
argument_list|)
argument_list|,
name|Types
operator|.
name|BLOB
operator|==
name|blobAttr
operator|.
name|getType
argument_list|()
operator|||
name|Types
operator|.
name|LONGVARBINARY
operator|==
name|blobAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|DbEntity
name|clobEnt
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"CLOB_TEST"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|clobEnt
argument_list|)
expr_stmt|;
name|DbAttribute
name|clobAttr
init|=
name|getDbAttribute
argument_list|(
name|clobEnt
argument_list|,
literal|"CLOB_COL"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|clobAttr
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|Types
operator|.
name|CLOB
argument_list|,
name|clobAttr
argument_list|)
argument_list|,
name|Types
operator|.
name|CLOB
operator|==
name|clobAttr
operator|.
name|getType
argument_list|()
operator|||
name|Types
operator|.
name|LONGVARCHAR
operator|==
name|clobAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertLobObjEntities
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|ObjEntity
name|blobEnt
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"BlobTest"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|blobEnt
argument_list|)
expr_stmt|;
comment|// BLOBs should be mapped as byte[]
name|ObjAttribute
name|blobAttr
init|=
operator|(
name|ObjAttribute
operator|)
name|blobEnt
operator|.
name|getAttribute
argument_list|(
literal|"blobCol"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"BlobTest.blobCol failed to load"
argument_list|,
name|blobAttr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"byte[]"
argument_list|,
name|blobAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|ObjEntity
name|clobEnt
init|=
name|map
operator|.
name|getObjEntity
argument_list|(
literal|"ClobTest"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|clobEnt
argument_list|)
expr_stmt|;
comment|// CLOBs should be mapped as Strings by default
name|ObjAttribute
name|clobAttr
init|=
operator|(
name|ObjAttribute
operator|)
name|clobEnt
operator|.
name|getAttribute
argument_list|(
literal|"clobCol"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|clobAttr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|clobAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|DbEntity
name|getDbEntity
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|DbEntity
name|de
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// sometimes table names get converted to lowercase
if|if
condition|(
name|de
operator|==
literal|null
condition|)
block|{
name|de
operator|=
name|map
operator|.
name|getDbEntity
argument_list|(
name|name
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|de
return|;
block|}
specifier|private
name|DbAttribute
name|getDbAttribute
parameter_list|(
name|DbEntity
name|ent
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|DbAttribute
name|da
init|=
operator|(
name|DbAttribute
operator|)
name|ent
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// sometimes table names get converted to lowercase
if|if
condition|(
name|da
operator|==
literal|null
condition|)
block|{
name|da
operator|=
operator|(
name|DbAttribute
operator|)
name|ent
operator|.
name|getAttribute
argument_list|(
name|name
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|da
return|;
block|}
specifier|private
name|DataMap
name|originalMap
parameter_list|()
block|{
return|return
operator|(
name|DataMap
operator|)
name|getNode
argument_list|()
operator|.
name|getDataMaps
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
comment|/**      * Selectively check how different types were processed.      */
specifier|public
name|void
name|checkTypes
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|DbEntity
name|dbe
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"PAINTING"
argument_list|)
decl_stmt|;
name|DbEntity
name|floatTest
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"FLOAT_TEST"
argument_list|)
decl_stmt|;
name|DbEntity
name|smallintTest
init|=
name|getDbEntity
argument_list|(
name|map
argument_list|,
literal|"SMALLINT_TEST"
argument_list|)
decl_stmt|;
name|DbAttribute
name|integerAttr
init|=
name|getDbAttribute
argument_list|(
name|dbe
argument_list|,
literal|"PAINTING_ID"
argument_list|)
decl_stmt|;
name|DbAttribute
name|decimalAttr
init|=
name|getDbAttribute
argument_list|(
name|dbe
argument_list|,
literal|"ESTIMATED_PRICE"
argument_list|)
decl_stmt|;
name|DbAttribute
name|varcharAttr
init|=
name|getDbAttribute
argument_list|(
name|dbe
argument_list|,
literal|"PAINTING_TITLE"
argument_list|)
decl_stmt|;
name|DbAttribute
name|floatAttr
init|=
name|getDbAttribute
argument_list|(
name|floatTest
argument_list|,
literal|"FLOAT_COL"
argument_list|)
decl_stmt|;
name|DbAttribute
name|smallintAttr
init|=
name|getDbAttribute
argument_list|(
name|smallintTest
argument_list|,
literal|"SMALLINT_COL"
argument_list|)
decl_stmt|;
comment|// check decimal
name|assertTrue
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|Types
operator|.
name|DECIMAL
argument_list|,
name|decimalAttr
argument_list|)
argument_list|,
name|Types
operator|.
name|DECIMAL
operator|==
name|decimalAttr
operator|.
name|getType
argument_list|()
operator|||
name|Types
operator|.
name|NUMERIC
operator|==
name|decimalAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|decimalAttr
operator|.
name|getScale
argument_list|()
argument_list|)
expr_stmt|;
comment|// check varchar
name|assertEquals
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|Types
operator|.
name|VARCHAR
argument_list|,
name|varcharAttr
argument_list|)
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|,
name|varcharAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|255
argument_list|,
name|varcharAttr
operator|.
name|getMaxLength
argument_list|()
argument_list|)
expr_stmt|;
comment|// check integer
name|assertEquals
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|,
name|integerAttr
argument_list|)
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|,
name|integerAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|// check float
name|assertTrue
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|Types
operator|.
name|FLOAT
argument_list|,
name|floatAttr
argument_list|)
argument_list|,
name|Types
operator|.
name|FLOAT
operator|==
name|floatAttr
operator|.
name|getType
argument_list|()
operator|||
name|Types
operator|.
name|DOUBLE
operator|==
name|floatAttr
operator|.
name|getType
argument_list|()
operator|||
name|Types
operator|.
name|REAL
operator|==
name|floatAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|// check smallint
name|assertTrue
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|Types
operator|.
name|SMALLINT
argument_list|,
name|smallintAttr
argument_list|)
argument_list|,
name|Types
operator|.
name|SMALLINT
operator|==
name|smallintAttr
operator|.
name|getType
argument_list|()
operator|||
name|Types
operator|.
name|INTEGER
operator|==
name|smallintAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|checkAllDBEntities
parameter_list|(
name|DataMap
name|map
parameter_list|)
block|{
name|Iterator
name|entIt
init|=
name|originalMap
argument_list|()
operator|.
name|getDbEntities
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|entIt
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbEntity
name|origEnt
init|=
operator|(
name|DbEntity
operator|)
name|entIt
operator|.
name|next
argument_list|()
decl_stmt|;
name|DbEntity
name|newEnt
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
name|origEnt
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|origEnt
operator|.
name|getAttributes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DbAttribute
name|origAttr
init|=
operator|(
name|DbAttribute
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|DbAttribute
name|newAttr
init|=
operator|(
name|DbAttribute
operator|)
name|newEnt
operator|.
name|getAttribute
argument_list|(
name|origAttr
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No matching DbAttribute for '"
operator|+
name|origAttr
operator|.
name|getName
argument_list|()
argument_list|,
name|newAttr
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|msgForTypeMismatch
argument_list|(
name|origAttr
argument_list|,
name|newAttr
argument_list|)
argument_list|,
name|origAttr
operator|.
name|getType
argument_list|()
argument_list|,
name|newAttr
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|// length and precision doesn't have to be the same
comment|// it must be greater or equal
name|assertTrue
argument_list|(
name|origAttr
operator|.
name|getMaxLength
argument_list|()
operator|<=
name|newAttr
operator|.
name|getMaxLength
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|origAttr
operator|.
name|getScale
argument_list|()
operator|<=
name|newAttr
operator|.
name|getScale
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|msgForTypeMismatch
parameter_list|(
name|DbAttribute
name|origAttr
parameter_list|,
name|DbAttribute
name|newAttr
parameter_list|)
block|{
return|return
name|msgForTypeMismatch
argument_list|(
name|origAttr
operator|.
name|getType
argument_list|()
argument_list|,
name|newAttr
argument_list|)
return|;
block|}
specifier|private
name|String
name|msgForTypeMismatch
parameter_list|(
name|int
name|origType
parameter_list|,
name|DbAttribute
name|newAttr
parameter_list|)
block|{
name|String
name|nt
init|=
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|newAttr
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|ot
init|=
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|origType
argument_list|)
decl_stmt|;
return|return
name|attrMismatch
argument_list|(
name|newAttr
operator|.
name|getName
argument_list|()
argument_list|,
literal|"expected type:<"
operator|+
name|ot
operator|+
literal|">, but was<"
operator|+
name|nt
operator|+
literal|">"
argument_list|)
return|;
block|}
specifier|private
name|String
name|attrMismatch
parameter_list|(
name|String
name|attrName
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"[Error loading attribute '"
argument_list|)
operator|.
name|append
argument_list|(
name|attrName
argument_list|)
operator|.
name|append
argument_list|(
literal|"': "
argument_list|)
operator|.
name|append
argument_list|(
name|msg
argument_list|)
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

