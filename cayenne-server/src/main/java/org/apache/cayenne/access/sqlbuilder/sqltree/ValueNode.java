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
operator|.
name|sqlbuilder
operator|.
name|sqltree
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Supplier
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
name|CayenneRuntimeException
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
name|ObjectId
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
name|access
operator|.
name|sqlbuilder
operator|.
name|SQLGenerationContext
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
name|translator
operator|.
name|DbAttributeBinding
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
name|sqlbuilder
operator|.
name|QuotingAppendable
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
name|ExtendedType
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

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|ValueNode
extends|extends
name|Node
block|{
specifier|private
specifier|final
name|Object
name|value
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|isArray
decl_stmt|;
comment|// Used as hint for type of this value
specifier|private
specifier|final
name|DbAttribute
name|attribute
decl_stmt|;
specifier|public
name|ValueNode
parameter_list|(
name|Object
name|value
parameter_list|,
name|boolean
name|isArray
parameter_list|,
name|DbAttribute
name|attribute
parameter_list|)
block|{
name|super
argument_list|(
name|NodeType
operator|.
name|VALUE
argument_list|)
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|isArray
operator|=
name|isArray
expr_stmt|;
name|this
operator|.
name|attribute
operator|=
name|attribute
expr_stmt|;
block|}
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|DbAttribute
name|getAttribute
parameter_list|()
block|{
return|return
name|attribute
return|;
block|}
specifier|public
name|boolean
name|isArray
parameter_list|()
block|{
return|return
name|isArray
return|;
block|}
annotation|@
name|Override
specifier|public
name|QuotingAppendable
name|append
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|appendValue
argument_list|(
name|value
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
return|return
name|buffer
return|;
block|}
specifier|protected
name|void
name|appendNullValue
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" NULL"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|appendValue
parameter_list|(
name|Object
name|val
parameter_list|,
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|appendNullValue
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|isArray
operator|&&
name|val
operator|.
name|getClass
argument_list|()
operator|.
name|isArray
argument_list|()
condition|)
block|{
if|if
condition|(
name|val
operator|instanceof
name|short
index|[]
condition|)
block|{
name|appendValue
argument_list|(
operator|(
name|short
index|[]
operator|)
name|val
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|val
operator|instanceof
name|char
index|[]
condition|)
block|{
name|appendValue
argument_list|(
operator|(
name|char
index|[]
operator|)
name|val
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|val
operator|instanceof
name|int
index|[]
condition|)
block|{
name|appendValue
argument_list|(
operator|(
name|int
index|[]
operator|)
name|val
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|val
operator|instanceof
name|long
index|[]
condition|)
block|{
name|appendValue
argument_list|(
operator|(
name|long
index|[]
operator|)
name|val
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|val
operator|instanceof
name|float
index|[]
condition|)
block|{
name|appendValue
argument_list|(
operator|(
name|float
index|[]
operator|)
name|val
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|val
operator|instanceof
name|double
index|[]
condition|)
block|{
name|appendValue
argument_list|(
operator|(
name|double
index|[]
operator|)
name|val
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|val
operator|instanceof
name|boolean
index|[]
condition|)
block|{
name|appendValue
argument_list|(
operator|(
name|boolean
index|[]
operator|)
name|val
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|val
operator|instanceof
name|Object
index|[]
condition|)
block|{
name|appendValue
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|val
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|val
operator|instanceof
name|byte
index|[]
condition|)
block|{
comment|// append byte[] array as single object
name|appendValue
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|val
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Unsupported array type %s"
argument_list|,
name|val
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|val
operator|instanceof
name|Persistent
condition|)
block|{
name|appendValue
argument_list|(
operator|(
name|Persistent
operator|)
name|val
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|val
operator|instanceof
name|ObjectId
condition|)
block|{
name|appendValue
argument_list|(
operator|(
name|ObjectId
operator|)
name|val
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|val
operator|instanceof
name|Supplier
condition|)
block|{
name|appendValue
argument_list|(
operator|(
operator|(
name|Supplier
argument_list|<
name|?
argument_list|>
operator|)
name|val
operator|)
operator|.
name|get
argument_list|()
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|val
operator|instanceof
name|CharSequence
condition|)
block|{
name|appendStringValue
argument_list|(
name|buffer
argument_list|,
operator|(
name|CharSequence
operator|)
name|val
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|appendObjectValue
argument_list|(
name|buffer
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|void
name|appendObjectValue
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|buffer
operator|.
name|getContext
argument_list|()
operator|==
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" ?"
argument_list|)
expr_stmt|;
name|addValueBinding
argument_list|(
name|buffer
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|appendStringValue
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|,
name|CharSequence
name|value
parameter_list|)
block|{
if|if
condition|(
name|buffer
operator|.
name|getContext
argument_list|()
operator|==
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|" '"
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// value can't be null here
name|buffer
operator|.
name|append
argument_list|(
literal|" ?"
argument_list|)
expr_stmt|;
name|addValueBinding
argument_list|(
name|buffer
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|addValueBinding
parameter_list|(
name|QuotingAppendable
name|buffer
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
comment|// value can't be null here
name|SQLGenerationContext
name|context
init|=
name|buffer
operator|.
name|getContext
argument_list|()
decl_stmt|;
comment|// allow translation in out-of-context scope, to be able to use as a standalone SQL generator
name|ExtendedType
argument_list|<
name|?
argument_list|>
name|extendedType
init|=
name|context
operator|.
name|getAdapter
argument_list|()
operator|.
name|getExtendedTypes
argument_list|()
operator|.
name|getRegisteredType
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|DbAttributeBinding
name|binding
init|=
operator|new
name|DbAttributeBinding
argument_list|(
name|attribute
argument_list|)
decl_stmt|;
name|binding
operator|.
name|setStatementPosition
argument_list|(
name|context
operator|.
name|getBindings
argument_list|()
operator|.
name|size
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setExtendedType
argument_list|(
name|extendedType
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|context
operator|.
name|getBindings
argument_list|()
operator|.
name|add
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|appendValue
parameter_list|(
name|Persistent
name|value
parameter_list|,
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|appendValue
argument_list|(
name|value
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|appendValue
parameter_list|(
name|ObjectId
name|value
parameter_list|,
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
for|for
control|(
name|Object
name|idVal
range|:
name|value
operator|.
name|getIdSnapshot
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|appendValue
argument_list|(
name|idVal
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|appendValue
parameter_list|(
name|short
index|[]
name|val
parameter_list|,
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|short
name|i
range|:
name|val
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|appendValue
argument_list|(
name|i
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|appendValue
parameter_list|(
name|char
index|[]
name|val
parameter_list|,
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|char
name|i
range|:
name|val
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|appendValue
argument_list|(
name|i
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|appendValue
parameter_list|(
name|int
index|[]
name|val
parameter_list|,
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|int
name|i
range|:
name|val
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|appendValue
argument_list|(
name|i
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|appendValue
parameter_list|(
name|long
index|[]
name|val
parameter_list|,
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|long
name|i
range|:
name|val
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|appendValue
argument_list|(
name|i
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|appendValue
parameter_list|(
name|float
index|[]
name|val
parameter_list|,
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|float
name|i
range|:
name|val
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|appendValue
argument_list|(
name|i
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|appendValue
parameter_list|(
name|double
index|[]
name|val
parameter_list|,
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|double
name|i
range|:
name|val
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|appendValue
argument_list|(
name|i
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|appendValue
parameter_list|(
name|boolean
index|[]
name|val
parameter_list|,
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|boolean
name|i
range|:
name|val
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|appendValue
argument_list|(
name|i
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|appendValue
parameter_list|(
name|byte
index|[]
name|val
parameter_list|,
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|byte
name|i
range|:
name|val
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|appendValue
argument_list|(
name|i
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|appendValue
parameter_list|(
name|Object
index|[]
name|val
parameter_list|,
name|QuotingAppendable
name|buffer
parameter_list|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Object
name|i
range|:
name|val
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|appendValue
argument_list|(
name|i
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Node
name|copy
parameter_list|()
block|{
return|return
operator|new
name|ValueNode
argument_list|(
name|value
argument_list|,
name|isArray
argument_list|,
name|attribute
argument_list|)
return|;
block|}
block|}
end_class

end_unit

