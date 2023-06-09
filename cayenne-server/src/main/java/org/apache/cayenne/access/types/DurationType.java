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
name|types
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|CallableStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
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
name|java
operator|.
name|time
operator|.
name|Duration
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
name|dba
operator|.
name|TypesMapping
import|;
end_import

begin_class
specifier|public
class|class
name|DurationType
implements|implements
name|ExtendedType
argument_list|<
name|Duration
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|Duration
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
specifier|protected
name|Object
name|convertToJdbcObject
parameter_list|(
name|Duration
name|val
parameter_list|,
name|int
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|INTEGER
condition|)
block|{
return|return
name|Long
operator|.
name|valueOf
argument_list|(
name|val
operator|.
name|toMillis
argument_list|()
argument_list|)
operator|.
name|intValue
argument_list|()
return|;
block|}
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|NUMERIC
condition|)
block|{
return|return
name|val
operator|.
name|toMillis
argument_list|()
return|;
block|}
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|DECIMAL
condition|)
block|{
return|return
name|val
operator|.
name|toMillis
argument_list|()
return|;
block|}
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|BIGINT
condition|)
block|{
return|return
name|val
operator|.
name|toMillis
argument_list|()
return|;
block|}
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|VARCHAR
condition|)
block|{
return|return
name|val
operator|.
name|toString
argument_list|()
return|;
block|}
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|LONGVARCHAR
condition|)
block|{
return|return
name|val
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Only INTEGER, NUMERIC, DECIMAL, BIGINT, VARCHAR, LONGVARCHAR "
operator|+
literal|"can be mapped as '"
operator|+
name|getClassName
argument_list|()
operator|+
literal|"', got "
operator|+
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|type
argument_list|)
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|Duration
name|value
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|scale
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|statement
operator|.
name|setNull
argument_list|(
name|pos
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|statement
operator|.
name|setObject
argument_list|(
name|pos
argument_list|,
name|convertToJdbcObject
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Duration
name|materializeObject
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|Duration
name|val
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|Types
operator|.
name|INTEGER
case|:
name|val
operator|=
name|Duration
operator|.
name|ofMillis
argument_list|(
name|rs
operator|.
name|getInt
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|NUMERIC
case|:
name|val
operator|=
name|Duration
operator|.
name|ofMillis
argument_list|(
name|rs
operator|.
name|getBigDecimal
argument_list|(
name|index
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|DECIMAL
case|:
name|val
operator|=
name|Duration
operator|.
name|ofMillis
argument_list|(
name|rs
operator|.
name|getBigDecimal
argument_list|(
name|index
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|BIGINT
case|:
name|val
operator|=
name|Duration
operator|.
name|ofMillis
argument_list|(
name|rs
operator|.
name|getLong
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|VARCHAR
case|:
name|val
operator|=
name|Duration
operator|.
name|parse
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|LONGVARCHAR
case|:
name|val
operator|=
name|Duration
operator|.
name|parse
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|rs
operator|.
name|wasNull
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|else if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
return|return
name|val
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't materialize "
operator|+
name|rs
operator|.
name|getObject
argument_list|(
name|index
argument_list|)
operator|+
literal|" of type: "
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Duration
name|materializeObject
parameter_list|(
name|CallableStatement
name|rs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|Duration
name|val
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|Types
operator|.
name|INTEGER
case|:
name|val
operator|=
name|Duration
operator|.
name|ofMillis
argument_list|(
name|rs
operator|.
name|getInt
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|NUMERIC
case|:
name|val
operator|=
name|Duration
operator|.
name|ofMillis
argument_list|(
name|rs
operator|.
name|getBigDecimal
argument_list|(
name|index
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|DECIMAL
case|:
name|val
operator|=
name|Duration
operator|.
name|ofMillis
argument_list|(
name|rs
operator|.
name|getBigDecimal
argument_list|(
name|index
argument_list|)
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|BIGINT
case|:
name|val
operator|=
name|Duration
operator|.
name|ofMillis
argument_list|(
name|rs
operator|.
name|getLong
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|VARCHAR
case|:
name|val
operator|=
name|Duration
operator|.
name|parse
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|LONGVARCHAR
case|:
name|val
operator|=
name|Duration
operator|.
name|parse
argument_list|(
name|rs
operator|.
name|getString
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|rs
operator|.
name|wasNull
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|else if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
return|return
name|val
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't materialize "
operator|+
name|rs
operator|.
name|getObject
argument_list|(
name|index
argument_list|)
operator|+
literal|" of type: "
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|(
name|Duration
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
return|return
literal|"NULL"
return|;
block|}
return|return
literal|"\'"
operator|+
name|value
operator|.
name|toString
argument_list|()
operator|+
literal|"\'"
return|;
block|}
block|}
end_class

end_unit

