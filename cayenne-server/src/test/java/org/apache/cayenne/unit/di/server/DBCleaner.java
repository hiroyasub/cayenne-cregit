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
name|di
operator|.
name|server
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
name|unit
operator|.
name|UnitDbAdapter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_class
specifier|public
class|class
name|DBCleaner
block|{
specifier|private
name|FlavoredDBHelper
name|dbHelper
decl_stmt|;
specifier|private
name|String
name|location
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|UnitDbAdapter
name|accessStackAdapter
decl_stmt|;
specifier|public
name|DBCleaner
parameter_list|(
name|FlavoredDBHelper
name|dbHelper
parameter_list|,
name|String
name|location
parameter_list|)
block|{
name|this
operator|.
name|dbHelper
operator|=
name|dbHelper
expr_stmt|;
name|this
operator|.
name|location
operator|=
name|location
expr_stmt|;
block|}
specifier|public
name|void
name|clean
parameter_list|()
throws|throws
name|SQLException
block|{
if|if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|TESTMAP_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PAINTING_INFO"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PAINTING"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PAINTING1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_EXHIBIT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_GROUP"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTGROUP"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"EXHIBIT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GALLERY"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARTIST_CT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GENERATED_COLUMN"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"NULL_TEST"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|MULTI_TIER_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_TABLE2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_TABLE1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_JOIN45"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_TABLE4"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MT_TABLE5"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|COMPOUND_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CHAR_FK_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CHAR_PK_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"COMPOUND_FK_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"COMPOUND_PK_TEST"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|PEOPLE_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ADDRESS"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DEPARTMENT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PERSON_NOTES"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PERSON"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CLIENT_COMPANY"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|BINARY_PK_PROJECT
argument_list|)
condition|)
block|{
if|if
condition|(
name|accessStackAdapter
operator|.
name|supportsBinaryPK
argument_list|()
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BINARY_PK_TEST2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BINARY_PK_TEST1"
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|DATE_TIME_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CALENDAR_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DATE_TEST"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|DELETE_RULES_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_CASCADE"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_DENY"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_NULLIFY"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_RULE"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|EMBEDDABLE_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"EMBED_ENTITY1"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|EMPTY_PROJECT
argument_list|)
condition|)
block|{
return|return;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|ENUM_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ENUM_ENTITY"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|EXTENDED_TYPE_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"EXTENDED_TYPE_TEST"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|GENERATED_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GENERATED_JOIN"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GENERATED_F1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GENERATED_F2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GENERATED_COLUMN_DEP"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GENERATED_COLUMN_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GENERATED_COLUMN_TEST2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GENERATED_COLUMN_COMP_KEY"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GENERATED_COLUMN_COMP_M"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|GENERIC_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GENERIC2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GENERIC1"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|INHERITANCE_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BASE_ENTITY"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DIRECT_TO_SUB_ENTITY"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"RELATED_ENTITY"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|INHERITANCE_SINGLE_TABLE1_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GROUP_MEMBERS"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"USER_PROPERTIES"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"GROUP_PROPERTIES"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ROLES"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|INHERITANCE_VERTICAL_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"IV_SUB1_SUB1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"IV_SUB1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"IV_SUB2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"IV_ROOT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"IV1_SUB1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"IV1_ROOT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"IV2_SUB1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"IV2_ROOT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"IV2_X"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|LIFECYCLES_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"LIFECYCLES"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|LOB_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CLOB_TEST_RELATION"
argument_list|)
expr_stmt|;
if|if
condition|(
name|accessStackAdapter
operator|.
name|supportsLobs
argument_list|()
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BLOB_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CLOB_TEST"
argument_list|)
expr_stmt|;
block|}
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TEST"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|LOCKING_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"LOCKING_HELPER"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"REL_LOCKING_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"SIMPLE_LOCKING_TEST"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|MAP_TO_MANY_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ID_MAP_TO_MANY_TARGET"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ID_MAP_TO_MANY"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MAP_TO_MANY_TARGET"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MAP_TO_MANY"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|MEANINGFUL_PK_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MEANINGFUL_PK"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MEANINGFUL_PK_DEP"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MEANINGFUL_PK_TEST1"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|MISC_TYPES_PROJECT
argument_list|)
condition|)
block|{
if|if
condition|(
name|accessStackAdapter
operator|.
name|supportsLobs
argument_list|()
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"SERIALIZABLE_ENTITY"
argument_list|)
expr_stmt|;
block|}
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ARRAYS_ENTITY"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CHARACTER_ENTITY"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|MIXED_PERSISTENCE_STRATEGY_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MIXED_PERSISTENCE_STRATEGY2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MIXED_PERSISTENCE_STRATEGY"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|MULTINODE_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CROSSDB_M1E1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CROSSDB_M2E1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CROSSDB_M2E2"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|NO_PK_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"NO_PK_TEST"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|NUMERIC_TYPES_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BIGDECIMAL_ENTITY"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BIGINTEGER_ENTITY"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BOOLEAN_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BIT_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"SMALLINT_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TINYINT_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DECIMAL_PK_TST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"FLOAT_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"LONG_ENTITY"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|ONEWAY_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"oneway_table2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"oneway_table1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"oneway_table4"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"oneway_table3"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|PERSISTENT_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CONTINENT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"COUNTRY"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|PRIMITIVE_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"PRIMITIVES_TEST"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|QUALIFIED_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TEST_QUALIFIED2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TEST_QUALIFIED1"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|QUOTED_IDENTIFIERS_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"quote Person"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"QUOTED_ADDRESS"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|REFLEXIVE_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"REFLEXIVE"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"FK_OF_DIFFERENT_TYPE"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"REFLEXIVE_AND_TO_ONE"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"RELATIONSHIP_HELPER"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MEANINGFUL_FK"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_ACTIVITY_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"ACTIVITY"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"RESULT"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_CHILD_MASTER_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CHILD"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"MASTER"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_CLOB_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CLOB_DETAIL"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"CLOB_MASTER"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_COLLECTION_TO_MANY_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"COLLECTION_TO_MANY_TARGET"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"COLLECTION_TO_MANY"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_DELETE_RULES_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_RULE_TEST3"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_RULE_TEST1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_RULE_TEST2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_RULE_JOIN"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_RULE_FLATB"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"DELETE_RULE_FLATA"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_FLATTENED_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"COMPLEX_JOIN"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"FLATTENED_TEST_4"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"FLATTENED_TEST_3"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"FLATTENED_TEST_2"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"FLATTENED_TEST_1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"FLATTENED_CIRCULAR_JOIN"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"FLATTENED_CIRCULAR"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_SET_TO_MANY_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"SET_TO_MANY_TARGET"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"SET_TO_MANY"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_TO_MANY_FK_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TO_MANY_FKDEP"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TO_MANY_FKROOT"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TO_MANY_ROOT2"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|RELATIONSHIPS_TO_ONE_FK_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TO_ONE_FK1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TO_ONE_FK2"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|RETURN_TYPES_PROJECT
argument_list|)
condition|)
block|{
if|if
condition|(
name|accessStackAdapter
operator|.
name|supportsLobs
argument_list|()
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TYPES_MAPPING_LOBS_TEST1"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TYPES_MAPPING_TEST2"
argument_list|)
expr_stmt|;
block|}
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TYPES_MAPPING_TEST1"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|SOFT_DELETE_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"SOFT_DELETE"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|SUS_PROJECT
argument_list|)
condition|)
block|{
return|return;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|TABLE_PRIMITIVES_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TABLE_PRIMITIVES"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|THINGS_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BALL"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BOX_THING"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"THING"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BOX_INFO"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BOX"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"BAG"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|TOONE_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TOONE_DEP"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"TOONE_MASTER"
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|location
operator|.
name|equals
argument_list|(
name|CayenneProjects
operator|.
name|UUID_PROJECT
argument_list|)
condition|)
block|{
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"UUID_TEST"
argument_list|)
expr_stmt|;
name|dbHelper
operator|.
name|deleteAll
argument_list|(
literal|"UUID_PK_ENTITY"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

