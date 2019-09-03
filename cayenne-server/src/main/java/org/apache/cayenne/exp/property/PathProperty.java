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

begin_comment
comment|/**  * Property that represents path segment (relationship or embeddable).  * Basically it provides {@code dot()} operator.  *  * @since 4.2  */
end_comment

begin_interface
specifier|public
interface|interface
name|PathProperty
parameter_list|<
name|E
parameter_list|>
extends|extends
name|Property
argument_list|<
name|E
argument_list|>
block|{
comment|/**      * Constructs a property path by appending the argument to the existing property separated by a dot.      *      * @return a newly created Property object.      */
specifier|default
name|BaseProperty
argument_list|<
name|Object
argument_list|>
name|dot
parameter_list|(
name|String
name|property
parameter_list|)
block|{
name|String
name|path
init|=
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|property
decl_stmt|;
return|return
name|PropertyFactory
operator|.
name|createBase
argument_list|(
name|path
argument_list|,
name|PropertyUtils
operator|.
name|buildExp
argument_list|(
name|path
argument_list|,
name|getExpression
argument_list|()
operator|.
name|getPathAliases
argument_list|()
argument_list|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Constructs a new property path by appending the argument to the existing property separated by a dot.      *      * @param property to append to path      * @return a newly created Property object.      */
specifier|default
parameter_list|<
name|T
parameter_list|>
name|BaseProperty
argument_list|<
name|T
argument_list|>
name|dot
parameter_list|(
name|BaseProperty
argument_list|<
name|T
argument_list|>
name|property
parameter_list|)
block|{
name|String
name|path
init|=
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|property
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
name|PropertyFactory
operator|.
name|createBase
argument_list|(
name|path
argument_list|,
name|PropertyUtils
operator|.
name|buildExp
argument_list|(
name|path
argument_list|,
name|getExpression
argument_list|()
operator|.
name|getPathAliases
argument_list|()
argument_list|)
argument_list|,
name|property
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Constructs a new property path by appending the argument to the existing property separated by a dot.      *      * @param property to append to path      * @return a newly created Property object.      */
specifier|default
parameter_list|<
name|T
extends|extends
name|Number
parameter_list|>
name|NumericProperty
argument_list|<
name|T
argument_list|>
name|dot
parameter_list|(
name|NumericProperty
argument_list|<
name|T
argument_list|>
name|property
parameter_list|)
block|{
name|String
name|path
init|=
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|property
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
name|PropertyFactory
operator|.
name|createNumeric
argument_list|(
name|path
argument_list|,
name|PropertyUtils
operator|.
name|buildExp
argument_list|(
name|path
argument_list|,
name|getExpression
argument_list|()
operator|.
name|getPathAliases
argument_list|()
argument_list|)
argument_list|,
name|property
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Constructs a new property path by appending the argument to the existing property separated by a dot.      *      * @param property to append to path      * @return a newly created Property object.      */
specifier|default
parameter_list|<
name|T
extends|extends
name|CharSequence
parameter_list|>
name|StringProperty
argument_list|<
name|T
argument_list|>
name|dot
parameter_list|(
name|StringProperty
argument_list|<
name|T
argument_list|>
name|property
parameter_list|)
block|{
name|String
name|path
init|=
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|property
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
name|PropertyFactory
operator|.
name|createString
argument_list|(
name|path
argument_list|,
name|PropertyUtils
operator|.
name|buildExp
argument_list|(
name|path
argument_list|,
name|getExpression
argument_list|()
operator|.
name|getPathAliases
argument_list|()
argument_list|)
argument_list|,
name|property
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Constructs a new property path by appending the argument to the existing property separated by a dot.      *      * @param property to append to path      * @return a newly created Property object.      */
specifier|default
parameter_list|<
name|T
parameter_list|>
name|DateProperty
argument_list|<
name|T
argument_list|>
name|dot
parameter_list|(
name|DateProperty
argument_list|<
name|T
argument_list|>
name|property
parameter_list|)
block|{
name|String
name|path
init|=
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|property
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
name|PropertyFactory
operator|.
name|createDate
argument_list|(
name|path
argument_list|,
name|PropertyUtils
operator|.
name|buildExp
argument_list|(
name|path
argument_list|,
name|getExpression
argument_list|()
operator|.
name|getPathAliases
argument_list|()
argument_list|)
argument_list|,
name|property
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

