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
name|unit
operator|.
name|di
operator|.
name|server
package|;
end_package

begin_class
specifier|public
class|class
name|CayenneProjects
block|{
comment|// known runtimes... unit tests may reuse these with @UseServerRuntime
comment|// annotation or
comment|// can define their own on the fly (TODO: how would that work with the
comment|// global schema
comment|// setup?)
specifier|public
specifier|static
specifier|final
name|String
name|ARRAY_TYPE_PROJECT
init|=
literal|"cayenne-array-type.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BINARY_PK_PROJECT
init|=
literal|"cayenne-binary-pk.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CAY_2032
init|=
literal|"cayenne-cay-2032.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|COMPOUND_PROJECT
init|=
literal|"cayenne-compound.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DATE_TIME_PROJECT
init|=
literal|"cayenne-date-time.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DELETE_RULES_PROJECT
init|=
literal|"cayenne-delete-rules.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EMBEDDABLE_PROJECT
init|=
literal|"cayenne-embeddable.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EMPTY_PROJECT
init|=
literal|"cayenne-empty.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ENUM_PROJECT
init|=
literal|"cayenne-enum.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|EXTENDED_TYPE_PROJECT
init|=
literal|"cayenne-extended-type.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GENERATED_PROJECT
init|=
literal|"cayenne-generated.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|GENERIC_PROJECT
init|=
literal|"cayenne-generic.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INHERITANCE_PROJECT
init|=
literal|"cayenne-inheritance.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INHERITANCE_SINGLE_TABLE1_PROJECT
init|=
literal|"cayenne-inheritance-single-table1.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INHERITANCE_VERTICAL_PROJECT
init|=
literal|"cayenne-inheritance-vertical.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LIFECYCLE_CALLBACKS_ORDER_PROJECT
init|=
literal|"cayenne-lifecycle-callbacks-order.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LIFECYCLES_PROJECT
init|=
literal|"cayenne-lifecycles.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LOB_PROJECT
init|=
literal|"cayenne-lob.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LOCKING_PROJECT
init|=
literal|"cayenne-locking.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MAP_TO_MANY_PROJECT
init|=
literal|"cayenne-map-to-many.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MEANINGFUL_PK_PROJECT
init|=
literal|"cayenne-meaningful-pk.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MISC_TYPES_PROJECT
init|=
literal|"cayenne-misc-types.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MIXED_PERSISTENCE_STRATEGY_PROJECT
init|=
literal|"cayenne-mixed-persistence-strategy.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MULTI_TIER_PROJECT
init|=
literal|"cayenne-multi-tier.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MULTINODE_PROJECT
init|=
literal|"cayenne-multinode.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NO_PK_PROJECT
init|=
literal|"cayenne-no-pk.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NUMERIC_TYPES_PROJECT
init|=
literal|"cayenne-numeric-types.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ONEWAY_PROJECT
init|=
literal|"cayenne-oneway-rels.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PEOPLE_PROJECT
init|=
literal|"cayenne-people.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PRIMITIVE_PROJECT
init|=
literal|"cayenne-primitive.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|QUALIFIED_PROJECT
init|=
literal|"cayenne-qualified.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|QUOTED_IDENTIFIERS_PROJECT
init|=
literal|"cayenne-quoted-identifiers.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|REFLEXIVE_PROJECT
init|=
literal|"cayenne-reflexive.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RELATIONSHIPS_PROJECT
init|=
literal|"cayenne-relationships.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RELATIONSHIPS_ACTIVITY_PROJECT
init|=
literal|"cayenne-relationships-activity.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RELATIONSHIPS_MANY_TO_MANY_JOIN_PROJECT
init|=
literal|"cayenne-relationships-many-to-many-join.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RELATIONSHIPS_CHILD_MASTER_PROJECT
init|=
literal|"cayenne-relationships-child-master.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RELATIONSHIPS_CLOB_PROJECT
init|=
literal|"cayenne-relationships-clob.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RELATIONSHIPS_COLLECTION_TO_MANY_PROJECT
init|=
literal|"cayenne-relationships-collection-to-many.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RELATIONSHIPS_DELETE_RULES_PROJECT
init|=
literal|"cayenne-relationships-delete-rules.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RELATIONSHIPS_FLATTENED_PROJECT
init|=
literal|"cayenne-relationships-flattened.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RELATIONSHIPS_SET_TO_MANY_PROJECT
init|=
literal|"cayenne-relationships-set-to-many.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RELATIONSHIPS_TO_MANY_FK_PROJECT
init|=
literal|"cayenne-relationships-to-many-fk.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RELATIONSHIPS_TO_ONE_FK_PROJECT
init|=
literal|"cayenne-relationships-to-one-fk.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|RETURN_TYPES_PROJECT
init|=
literal|"cayenne-return-types.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOFT_DELETE_PROJECT
init|=
literal|"cayenne-soft-delete.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SUS_PROJECT
init|=
literal|"cayenne-sus.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TABLE_PRIMITIVES_PROJECT
init|=
literal|"cayenne-table-primitives.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TESTMAP_PROJECT
init|=
literal|"cayenne-testmap.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|THINGS_PROJECT
init|=
literal|"cayenne-things.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOONE_PROJECT
init|=
literal|"cayenne-toone.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UNSUPPORTED_DISTINCT_TYPES_PROJECT
init|=
literal|"cayenne-unsupported-distinct-types.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|UUID_PROJECT
init|=
literal|"cayenne-uuid.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CUSTOM_NAME_PROJECT
init|=
literal|"custom-name-file.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WEIGHTED_SORT_PROJECT
init|=
literal|"cayenne-weighted-sort.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HYBRID_DATA_OBJECT_PROJECT
init|=
literal|"cayenne-hybrid-data-object.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA8
init|=
literal|"cayenne-java8.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|INHERITANCE_WITH_ENUM_PROJECT
init|=
literal|"cayenne-inheritance-with-enum.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LAZY_ATTRIBUTES_PROJECT
init|=
literal|"cayenne-lazy-attributes.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CAY_2666
init|=
literal|"cay2666/cayenne-cay-2666.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CAY_2641
init|=
literal|"cay2641/cayenne-cay-2641.xml"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ANNOTATION
init|=
literal|"annotation/cayenne-project.xml"
decl_stmt|;
block|}
end_class

end_unit

