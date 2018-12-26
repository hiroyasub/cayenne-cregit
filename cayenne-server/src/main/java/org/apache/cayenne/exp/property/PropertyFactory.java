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
name|exp
operator|.
name|property
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDateTime
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
name|ObjectContext
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
name|Persistent
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
name|exp
operator|.
name|ExpressionFactory
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
name|FunctionExpressionFactory
import|;
end_import

begin_comment
comment|/**  *  * Factory class that produces all property types.  *  * @see org.apache.cayenne.exp.property  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|PropertyFactory
block|{
comment|/**      * Property that can be used to select {@code COUNT(*)}      *<p>      * Usage:<pre>{@code      * ObjectSelect.columnQuery(Artist.class, Artist.ARTIST_NAME, PropertyFactory.COUNT);      * }</pre>      * @see org.apache.cayenne.query.ObjectSelect#selectCount(ObjectContext)      */
specifier|public
specifier|static
specifier|final
name|NumericProperty
argument_list|<
name|Long
argument_list|>
name|COUNT
init|=
name|createNumeric
argument_list|(
name|FunctionExpressionFactory
operator|.
name|countExp
argument_list|()
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Property that corresponds to SQL function {@code NOW()}      *<p>      * Usage:<pre>{@code      * ObjectSelect.query(Artist.class).where(Artist.DATE_OF_BIRTH.year().lt(PropertyFactory.NOW.year().sub(100)));      * }</pre>      */
specifier|public
specifier|static
specifier|final
name|DateProperty
argument_list|<
name|LocalDateTime
argument_list|>
name|NOW
init|=
name|createDate
argument_list|(
name|FunctionExpressionFactory
operator|.
name|currentTimestamp
argument_list|()
argument_list|,
name|LocalDateTime
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// BaseProperty
comment|/**      * Create base property      *      * @param name of the property      * @param expression that property will use      * @param type type of represented attribute      * @param<T> type of represented attribute      * @return new property with custom expression      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|BaseProperty
argument_list|<
name|T
argument_list|>
name|createBase
parameter_list|(
name|String
name|name
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|BaseProperty
argument_list|<>
argument_list|(
name|name
argument_list|,
name|expression
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Create base property      *      * @param name of the property, will be used as value for path expression      * @param type type of represented attribute      * @param<T> type of represented attribute      * @return new path property      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|BaseProperty
argument_list|<
name|T
argument_list|>
name|createBase
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|createBase
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Create base property      *      * @param expression that property will use      * @param type type of represented attribute      * @param<T> type of represented attribute      * @return new property with custom expression without name      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|BaseProperty
argument_list|<
name|T
argument_list|>
name|createBase
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|createBase
argument_list|(
literal|null
argument_list|,
name|expression
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|// StringProperty
comment|/**      * Create string property      *      * @param name of the property      * @param expression that property will use      * @param type type of represented attribute      * @param<T> type of represented attribute      * @return new property with custom expression      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|CharSequence
parameter_list|>
name|StringProperty
argument_list|<
name|T
argument_list|>
name|createString
parameter_list|(
name|String
name|name
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|StringProperty
argument_list|<>
argument_list|(
name|name
argument_list|,
name|expression
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Create string property      *      * @param name of the property, will be used as value for path expression      * @param type type of represented attribute      * @param<T> type of represented attribute      * @return new path property      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|CharSequence
parameter_list|>
name|StringProperty
argument_list|<
name|T
argument_list|>
name|createString
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|createString
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Create string property      *      * @param expression that property will use      * @param type type of represented attribute      * @param<T> type of represented attribute      * @return new property with custom expression without name      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|CharSequence
parameter_list|>
name|StringProperty
argument_list|<
name|T
argument_list|>
name|createString
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|createString
argument_list|(
literal|null
argument_list|,
name|expression
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|// NumericProperty
comment|/**      * Create numeric property      *      * @param name of the property      * @param expression that property will use      * @param type type of represented attribute      * @param<T> type of represented attribute      * @return new property with custom expression      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Number
parameter_list|>
name|NumericProperty
argument_list|<
name|T
argument_list|>
name|createNumeric
parameter_list|(
name|String
name|name
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|NumericProperty
argument_list|<>
argument_list|(
name|name
argument_list|,
name|expression
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Create numeric property      *      * @param name of the property, will be used as value for path expression      * @param type type of represented attribute      * @param<T> type of represented attribute      * @return new path property      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Number
parameter_list|>
name|NumericProperty
argument_list|<
name|T
argument_list|>
name|createNumeric
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|createNumeric
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Create numeric property      *      * @param expression that property will use      * @param type type of represented attribute      * @param<T> type of represented attribute      * @return new property with custom expression without name      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Number
parameter_list|>
name|NumericProperty
argument_list|<
name|T
argument_list|>
name|createNumeric
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|createNumeric
argument_list|(
literal|null
argument_list|,
name|expression
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|// DateProperty
comment|/**      * Create date property      *      * @param name of the property      * @param expression that property will use      * @param type type of represented attribute      * @param<T> type of represented attribute      * @return new property with custom expression      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|DateProperty
argument_list|<
name|T
argument_list|>
name|createDate
parameter_list|(
name|String
name|name
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|new
name|DateProperty
argument_list|<>
argument_list|(
name|name
argument_list|,
name|expression
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Create date property      *      * @param name of the property, will be used as value for path expression      * @param type type of represented attribute      * @param<T> type of represented attribute      * @return new path property      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|DateProperty
argument_list|<
name|T
argument_list|>
name|createDate
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|createDate
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Create date property      *      * @param expression that property will use      * @param type type of represented attribute      * @param<T> type of represented attribute      * @return new property with custom expression without name      */
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|DateProperty
argument_list|<
name|T
argument_list|>
name|createDate
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|createDate
argument_list|(
literal|null
argument_list|,
name|expression
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|// ToOne relationship property
comment|/**      * Create entity property      *      * @param name of the property      * @param expression that property will use      * @param entityType type of represented relationship entity      * @param<T> type of represented relationship entity      * @return new property with custom expression      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Persistent
parameter_list|>
name|EntityProperty
argument_list|<
name|T
argument_list|>
name|createEntity
parameter_list|(
name|String
name|name
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|entityType
parameter_list|)
block|{
return|return
operator|new
name|EntityProperty
argument_list|<>
argument_list|(
name|name
argument_list|,
name|expression
argument_list|,
name|entityType
argument_list|)
return|;
block|}
comment|/**      * Create entity property      *      * @param name of the property, will be used as value for path expression      * @param type type of represented relationship entity      * @param<T> type of represented relationship entity      * @return new path property      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Persistent
parameter_list|>
name|EntityProperty
argument_list|<
name|T
argument_list|>
name|createEntity
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|createEntity
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Create entity property      *      * @param expression that property will use      * @param type type of represented relationship entity      * @param<T> type of represented relationship entity      * @return new property with custom expression without name      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Persistent
parameter_list|>
name|EntityProperty
argument_list|<
name|T
argument_list|>
name|createEntity
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|createEntity
argument_list|(
literal|null
argument_list|,
name|expression
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|// Self properties
comment|/**      *<b>Self</b> property allows to create column queries that return      * full objects along with custom column set.      *<p>      *  Usage example, query will return object with dependent objects count:<pre>{@code      *  List<Object[]> result = ObjectSelect.columnQuery(Artist.class,      *          PropertyFactory.createSelf(Artist.class),      *          Artist.PAINTING_ARRAY.count())      *      .select(context); }</pre>      *      * @param type of represented entity      * @param<T> type of represented entity      * @return new 'self' property      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Persistent
parameter_list|>
name|EntityProperty
argument_list|<
name|T
argument_list|>
name|createSelf
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|createEntity
argument_list|(
name|ExpressionFactory
operator|.
name|fullObjectExp
argument_list|()
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      *<b>Self</b> property allows to create column queries that return      * full objects along with custom column set.      *<p>      *  This method is not much useful, as to-one property can be used as is in this case,      *  example is purely for demonstration purpose only. See {@link EntityProperty} usage examples.      *<p>      *  Usage example, query will return object with dependent objects count:<pre>{@code      *  List<Object[]> result = ObjectSelect.columnQuery(Painting.class,      *          Painting.PAINTING_TITLE,      *          PropertyFactory.createSelf(Painting.TO_ARTIST.getExpression(), Painting.class))      *      .select(context); }</pre>      *      * @param expression expression to be used for this property (usually it will be path from other property)      * @param type of represented entity      * @param<T> type of represented entity      * @return new 'self' property      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Persistent
parameter_list|>
name|EntityProperty
argument_list|<
name|T
argument_list|>
name|createSelf
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|createEntity
argument_list|(
name|ExpressionFactory
operator|.
name|fullObjectExp
argument_list|(
name|expression
argument_list|)
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|// ToMany relationship properties
comment|/**      * Create to-many relationship mapped on list property      *      * @param name of the property      * @param expression that property will use      * @param entityType type of represented relationship entity      * @param<T> type of represented relationship entity      * @return new property with custom expression      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Persistent
parameter_list|>
name|ListProperty
argument_list|<
name|T
argument_list|>
name|createList
parameter_list|(
name|String
name|name
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|entityType
parameter_list|)
block|{
return|return
operator|new
name|ListProperty
argument_list|<>
argument_list|(
name|name
argument_list|,
name|expression
argument_list|,
name|entityType
argument_list|)
return|;
block|}
comment|/**      * Create to-many relationship mapped on list property      *      * @param name of the property, will be used as value for path expression      * @param entityType type of represented relationship entity      * @param<T> type of represented relationship entity      * @return new path property      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Persistent
parameter_list|>
name|ListProperty
argument_list|<
name|T
argument_list|>
name|createList
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|entityType
parameter_list|)
block|{
return|return
name|createList
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
name|entityType
argument_list|)
return|;
block|}
comment|/**      * Create to-many relationship mapped on set property      *      * @param name of the property      * @param expression that property will use      * @param entityType type of represented attribute      * @param<T> type of represented attribute      * @return new property with custom expression      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Persistent
parameter_list|>
name|SetProperty
argument_list|<
name|T
argument_list|>
name|createSet
parameter_list|(
name|String
name|name
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|entityType
parameter_list|)
block|{
return|return
operator|new
name|SetProperty
argument_list|<>
argument_list|(
name|name
argument_list|,
name|expression
argument_list|,
name|entityType
argument_list|)
return|;
block|}
comment|/**      * Create to-many relationship mapped on set property      *      * @param name of the property, will be used as value for path expression      * @param entityType type of represented relationship entity      * @param<T> type of represented relationship entity      * @return new path property      */
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Persistent
parameter_list|>
name|SetProperty
argument_list|<
name|T
argument_list|>
name|createSet
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|entityType
parameter_list|)
block|{
return|return
name|createSet
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
name|entityType
argument_list|)
return|;
block|}
comment|/**      * Create to-many relationship mapped on map property      *      * @param name of the property      * @param expression that property will use      * @param keyType type of represented relationship keys      * @param entityType type of represented relationship values      * @param<K> type of represented relationship keys      * @param<V> type of represented relationship values      * @return new property with custom expression      */
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
extends|extends
name|Persistent
parameter_list|>
name|MapProperty
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createMap
parameter_list|(
name|String
name|name
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Class
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|,
name|Class
argument_list|<
name|V
argument_list|>
name|entityType
parameter_list|)
block|{
return|return
operator|new
name|MapProperty
argument_list|<>
argument_list|(
name|name
argument_list|,
name|expression
argument_list|,
name|keyType
argument_list|,
name|entityType
argument_list|)
return|;
block|}
comment|/**      * Create to-many relationship mapped on map property      *      * @param name of the property, will be used as value for path expression      * @param keyType type of represented relationship keys      * @param entityType type of represented relationship values      * @param<K> type of represented relationship keys      * @param<V> type of represented relationship values      * @return new path property      */
specifier|public
specifier|static
parameter_list|<
name|K
parameter_list|,
name|V
extends|extends
name|Persistent
parameter_list|>
name|MapProperty
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|createMap
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|,
name|Class
argument_list|<
name|V
argument_list|>
name|entityType
parameter_list|)
block|{
return|return
name|createMap
argument_list|(
name|name
argument_list|,
literal|null
argument_list|,
name|keyType
argument_list|,
name|entityType
argument_list|)
return|;
block|}
block|}
end_class

end_unit

