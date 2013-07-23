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
name|util
operator|.
name|Date
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

begin_comment
comment|/**  * Maps<code>java.util.Date</code> to any of the three database date/time types: TIME,  * DATE, TIMESTAMP.  */
end_comment

begin_class
specifier|public
class|class
name|UtilDateType
implements|implements
name|ExtendedType
block|{
comment|/**      * Returns "java.util.Date".      */
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|Date
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
name|Object
name|val
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|DATE
condition|)
return|return
operator|new
name|java
operator|.
name|sql
operator|.
name|Date
argument_list|(
operator|(
operator|(
name|Date
operator|)
name|val
operator|)
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|TIME
condition|)
return|return
operator|new
name|java
operator|.
name|sql
operator|.
name|Time
argument_list|(
operator|(
operator|(
name|Date
operator|)
name|val
operator|)
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|TIMESTAMP
condition|)
return|return
operator|new
name|java
operator|.
name|sql
operator|.
name|Timestamp
argument_list|(
operator|(
operator|(
name|Date
operator|)
name|val
operator|)
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
else|else
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Only DATE, TIME or TIMESTAMP can be mapped as '"
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
specifier|public
name|Date
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
name|Date
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
name|TIMESTAMP
case|:
name|val
operator|=
name|rs
operator|.
name|getTimestamp
argument_list|(
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|DATE
case|:
name|val
operator|=
name|rs
operator|.
name|getDate
argument_list|(
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|TIME
case|:
name|val
operator|=
name|rs
operator|.
name|getTime
argument_list|(
name|index
argument_list|)
expr_stmt|;
break|break;
default|default:
name|val
operator|=
name|rs
operator|.
name|getTimestamp
argument_list|(
name|index
argument_list|)
expr_stmt|;
break|break;
block|}
comment|// return java.util.Date instead of subclass
return|return
name|val
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|Date
argument_list|(
name|val
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Date
name|materializeObject
parameter_list|(
name|CallableStatement
name|cs
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
name|Date
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
name|TIMESTAMP
case|:
name|val
operator|=
name|cs
operator|.
name|getTimestamp
argument_list|(
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|DATE
case|:
name|val
operator|=
name|cs
operator|.
name|getDate
argument_list|(
name|index
argument_list|)
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|TIME
case|:
name|val
operator|=
name|cs
operator|.
name|getTime
argument_list|(
name|index
argument_list|)
expr_stmt|;
break|break;
default|default:
name|val
operator|=
name|cs
operator|.
name|getTimestamp
argument_list|(
name|index
argument_list|)
expr_stmt|;
break|break;
block|}
comment|// return java.util.Date instead of subclass
return|return
name|val
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|Date
argument_list|(
name|val
operator|.
name|getTime
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|Object
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
block|}
end_class

end_unit
