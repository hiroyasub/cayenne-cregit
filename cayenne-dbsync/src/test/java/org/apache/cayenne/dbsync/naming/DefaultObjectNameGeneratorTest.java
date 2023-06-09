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
name|dbsync
operator|.
name|naming
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
specifier|public
class|class
name|DefaultObjectNameGeneratorTest
block|{
specifier|private
name|DefaultObjectNameGenerator
name|generator
init|=
operator|new
name|DefaultObjectNameGenerator
argument_list|(
name|NoStemStemmer
operator|.
name|getInstance
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|DbRelationship
name|makeRelationship
parameter_list|(
name|String
name|srcEntity
parameter_list|,
name|String
name|srcKey
parameter_list|,
name|String
name|targetEntity
parameter_list|,
name|String
name|targetKey
parameter_list|,
name|boolean
name|toMany
parameter_list|)
block|{
name|DbRelationship
name|relationship
init|=
operator|new
name|DbRelationship
argument_list|()
decl_stmt|;
name|relationship
operator|.
name|addJoin
argument_list|(
operator|new
name|DbJoin
argument_list|(
name|relationship
argument_list|,
name|srcKey
argument_list|,
name|targetKey
argument_list|)
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setToMany
argument_list|(
name|toMany
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setSourceEntity
argument_list|(
operator|new
name|DbEntity
argument_list|(
name|srcEntity
argument_list|)
argument_list|)
expr_stmt|;
name|relationship
operator|.
name|setTargetEntityName
argument_list|(
name|targetEntity
argument_list|)
expr_stmt|;
return|return
name|relationship
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRelationshipName_LowerCase_Underscores
parameter_list|()
block|{
name|DbRelationship
name|r1
init|=
name|makeRelationship
argument_list|(
literal|"painting"
argument_list|,
literal|"artist_id"
argument_list|,
literal|"artist"
argument_list|,
literal|"artist_id"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"artist"
argument_list|,
name|generator
operator|.
name|relationshipName
argument_list|(
name|r1
argument_list|)
argument_list|)
expr_stmt|;
name|DbRelationship
name|r2
init|=
name|makeRelationship
argument_list|(
literal|"artist"
argument_list|,
literal|"artist_id"
argument_list|,
literal|"painting"
argument_list|,
literal|"artist_id"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"paintings"
argument_list|,
name|generator
operator|.
name|relationshipName
argument_list|(
name|r2
argument_list|)
argument_list|)
expr_stmt|;
name|DbRelationship
name|r3
init|=
name|makeRelationship
argument_list|(
literal|"person"
argument_list|,
literal|"mother_id"
argument_list|,
literal|"person"
argument_list|,
literal|"person_id"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"mother"
argument_list|,
name|generator
operator|.
name|relationshipName
argument_list|(
name|r3
argument_list|)
argument_list|)
expr_stmt|;
name|DbRelationship
name|r4
init|=
name|makeRelationship
argument_list|(
literal|"person"
argument_list|,
literal|"person_id"
argument_list|,
literal|"person"
argument_list|,
literal|"mother_id"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"people"
argument_list|,
name|generator
operator|.
name|relationshipName
argument_list|(
name|r4
argument_list|)
argument_list|)
expr_stmt|;
name|DbRelationship
name|r5
init|=
name|makeRelationship
argument_list|(
literal|"person"
argument_list|,
literal|"shipping_address_id"
argument_list|,
literal|"address"
argument_list|,
literal|"id"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"shippingAddress"
argument_list|,
name|generator
operator|.
name|relationshipName
argument_list|(
name|r5
argument_list|)
argument_list|)
expr_stmt|;
name|DbRelationship
name|r6
init|=
name|makeRelationship
argument_list|(
literal|"person"
argument_list|,
literal|"id"
argument_list|,
literal|"address"
argument_list|,
literal|"person_id"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"addresses"
argument_list|,
name|generator
operator|.
name|relationshipName
argument_list|(
name|r6
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRelationshipName_UpperCase_Underscores
parameter_list|()
block|{
name|DbRelationship
name|r1
init|=
name|makeRelationship
argument_list|(
literal|"PAINTING"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"ARTIST"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"artist"
argument_list|,
name|generator
operator|.
name|relationshipName
argument_list|(
name|r1
argument_list|)
argument_list|)
expr_stmt|;
name|DbRelationship
name|r2
init|=
name|makeRelationship
argument_list|(
literal|"ARTIST"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|"PAINTING"
argument_list|,
literal|"ARTIST_ID"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"paintings"
argument_list|,
name|generator
operator|.
name|relationshipName
argument_list|(
name|r2
argument_list|)
argument_list|)
expr_stmt|;
name|DbRelationship
name|r3
init|=
name|makeRelationship
argument_list|(
literal|"PERSON"
argument_list|,
literal|"MOTHER_ID"
argument_list|,
literal|"PERSON"
argument_list|,
literal|"PERSON_ID"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"mother"
argument_list|,
name|generator
operator|.
name|relationshipName
argument_list|(
name|r3
argument_list|)
argument_list|)
expr_stmt|;
name|DbRelationship
name|r4
init|=
name|makeRelationship
argument_list|(
literal|"PERSON"
argument_list|,
literal|"PERSON_ID"
argument_list|,
literal|"PERSON"
argument_list|,
literal|"MOTHER_ID"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"people"
argument_list|,
name|generator
operator|.
name|relationshipName
argument_list|(
name|r4
argument_list|)
argument_list|)
expr_stmt|;
name|DbRelationship
name|r5
init|=
name|makeRelationship
argument_list|(
literal|"PERSON"
argument_list|,
literal|"SHIPPING_ADDRESS_ID"
argument_list|,
literal|"ADDRESS"
argument_list|,
literal|"ID"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"shippingAddress"
argument_list|,
name|generator
operator|.
name|relationshipName
argument_list|(
name|r5
argument_list|)
argument_list|)
expr_stmt|;
name|DbRelationship
name|r6
init|=
name|makeRelationship
argument_list|(
literal|"PERSON"
argument_list|,
literal|"ID"
argument_list|,
literal|"ADDRESS"
argument_list|,
literal|"PERSON_ID"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"addresses"
argument_list|,
name|generator
operator|.
name|relationshipName
argument_list|(
name|r6
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjEntityName
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"Artist"
argument_list|,
name|generator
operator|.
name|objEntityName
argument_list|(
operator|new
name|DbEntity
argument_list|(
literal|"ARTIST"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ArtistWork"
argument_list|,
name|generator
operator|.
name|objEntityName
argument_list|(
operator|new
name|DbEntity
argument_list|(
literal|"ARTIST_WORK"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testObjAttributeName
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"name"
argument_list|,
name|generator
operator|.
name|objAttributeName
argument_list|(
operator|new
name|DbAttribute
argument_list|(
literal|"NAME"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artistName"
argument_list|,
name|generator
operator|.
name|objAttributeName
argument_list|(
operator|new
name|DbAttribute
argument_list|(
literal|"ARTIST_NAME"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

