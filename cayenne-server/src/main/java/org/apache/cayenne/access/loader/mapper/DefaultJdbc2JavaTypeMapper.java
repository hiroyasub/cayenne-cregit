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
name|loader
operator|.
name|mapper
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
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
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
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_comment
comment|/**  * @since 4.0.  */
end_comment

begin_class
specifier|public
class|class
name|DefaultJdbc2JavaTypeMapper
implements|implements
name|Jdbc2JavaTypeMapper
block|{
comment|// Never use "-1" or any other normal integer, since there
comment|// is a big chance it is being reserved in java.sql.Types
specifier|public
specifier|static
specifier|final
name|int
name|NOT_DEFINED
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
comment|// char constants for Java data types
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_LONG
init|=
literal|"java.lang.Long"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_BYTES
init|=
literal|"byte[]"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_BOOLEAN
init|=
literal|"java.lang.Boolean"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_STRING
init|=
literal|"java.lang.String"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_SQLDATE
init|=
literal|"java.sql.Date"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_UTILDATE
init|=
literal|"java.util.Date"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_BIGDECIMAL
init|=
literal|"java.math.BigDecimal"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_DOUBLE
init|=
literal|"java.lang.Double"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_FLOAT
init|=
literal|"java.lang.Float"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_INTEGER
init|=
literal|"java.lang.Integer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_SHORT
init|=
literal|"java.lang.Short"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_BYTE
init|=
literal|"java.lang.Byte"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_TIME
init|=
literal|"java.sql.Time"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_TIMESTAMP
init|=
literal|"java.sql.Timestamp"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|JAVA_BLOB
init|=
literal|"java.sql.Blob"
decl_stmt|;
comment|/**      * Keys: java class names, Values: SQL int type definitions from java.sql.Types      */
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|javaSqlEnum
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|DbType
argument_list|,
name|String
argument_list|>
name|mapping
init|=
operator|new
name|HashMap
argument_list|<
name|DbType
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|SortedSet
argument_list|<
name|DbType
argument_list|>
name|dbTypes
init|=
operator|new
name|TreeSet
argument_list|<
name|DbType
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|classToPrimitive
decl_stmt|;
block|{
name|add
parameter_list|(
name|Types
operator|.
name|BIGINT
parameter_list|,
name|JAVA_LONG
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|BINARY
parameter_list|,
name|JAVA_BYTES
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|BIT
parameter_list|,
name|JAVA_BOOLEAN
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|BOOLEAN
parameter_list|,
name|JAVA_BOOLEAN
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|BLOB
parameter_list|,
name|JAVA_BYTES
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|CLOB
parameter_list|,
name|JAVA_STRING
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|CHAR
parameter_list|,
name|JAVA_STRING
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|DATE
parameter_list|,
name|JAVA_UTILDATE
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|DECIMAL
parameter_list|,
name|JAVA_BIGDECIMAL
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|DOUBLE
parameter_list|,
name|JAVA_DOUBLE
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|FLOAT
parameter_list|,
name|JAVA_FLOAT
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|INTEGER
parameter_list|,
name|JAVA_INTEGER
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|LONGVARCHAR
parameter_list|,
name|JAVA_STRING
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|LONGVARBINARY
parameter_list|,
name|JAVA_BYTES
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|NUMERIC
parameter_list|,
name|JAVA_BIGDECIMAL
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|REAL
parameter_list|,
name|JAVA_FLOAT
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|SMALLINT
parameter_list|,
name|JAVA_SHORT
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|TINYINT
parameter_list|,
name|JAVA_SHORT
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|TIME
parameter_list|,
name|JAVA_UTILDATE
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|TIMESTAMP
parameter_list|,
name|JAVA_UTILDATE
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|VARBINARY
parameter_list|,
name|JAVA_BYTES
parameter_list|)
constructor_decl|;
name|add
parameter_list|(
name|Types
operator|.
name|VARCHAR
parameter_list|,
name|JAVA_STRING
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_LONG
parameter_list|,
name|Types
operator|.
name|BIGINT
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_BYTES
parameter_list|,
name|Types
operator|.
name|BINARY
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_BOOLEAN
parameter_list|,
name|Types
operator|.
name|BIT
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_STRING
parameter_list|,
name|Types
operator|.
name|VARCHAR
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_SQLDATE
parameter_list|,
name|Types
operator|.
name|DATE
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_UTILDATE
parameter_list|,
name|Types
operator|.
name|DATE
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_TIMESTAMP
parameter_list|,
name|Types
operator|.
name|TIMESTAMP
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_BIGDECIMAL
parameter_list|,
name|Types
operator|.
name|DECIMAL
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_DOUBLE
parameter_list|,
name|Types
operator|.
name|DOUBLE
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_FLOAT
parameter_list|,
name|Types
operator|.
name|FLOAT
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_INTEGER
parameter_list|,
name|Types
operator|.
name|INTEGER
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_SHORT
parameter_list|,
name|Types
operator|.
name|SMALLINT
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_BYTE
parameter_list|,
name|Types
operator|.
name|SMALLINT
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_TIME
parameter_list|,
name|Types
operator|.
name|TIME
parameter_list|)
constructor_decl|;
name|javaSqlEnum
operator|.
name|put
parameter_list|(
name|JAVA_TIMESTAMP
parameter_list|,
name|Types
operator|.
name|TIMESTAMP
parameter_list|)
constructor_decl|;
comment|// add primitives
name|javaSqlEnum
operator|.
name|put
argument_list|(
literal|"byte"
argument_list|,
name|Types
operator|.
name|TINYINT
argument_list|)
expr_stmt|;
name|javaSqlEnum
operator|.
name|put
argument_list|(
literal|"int"
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|)
expr_stmt|;
name|javaSqlEnum
operator|.
name|put
argument_list|(
literal|"short"
argument_list|,
name|Types
operator|.
name|SMALLINT
argument_list|)
expr_stmt|;
name|javaSqlEnum
operator|.
name|put
argument_list|(
literal|"char"
argument_list|,
name|Types
operator|.
name|CHAR
argument_list|)
expr_stmt|;
name|javaSqlEnum
operator|.
name|put
argument_list|(
literal|"double"
argument_list|,
name|Types
operator|.
name|DOUBLE
argument_list|)
expr_stmt|;
name|javaSqlEnum
operator|.
name|put
argument_list|(
literal|"long"
argument_list|,
name|Types
operator|.
name|BIGINT
argument_list|)
expr_stmt|;
name|javaSqlEnum
operator|.
name|put
argument_list|(
literal|"float"
argument_list|,
name|Types
operator|.
name|FLOAT
argument_list|)
expr_stmt|;
name|javaSqlEnum
operator|.
name|put
argument_list|(
literal|"boolean"
argument_list|,
name|Types
operator|.
name|BIT
argument_list|)
expr_stmt|;
name|classToPrimitive
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|classToPrimitive
operator|.
name|put
argument_list|(
name|Byte
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"byte"
argument_list|)
expr_stmt|;
name|classToPrimitive
operator|.
name|put
argument_list|(
name|Long
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"long"
argument_list|)
expr_stmt|;
name|classToPrimitive
operator|.
name|put
argument_list|(
name|Double
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"double"
argument_list|)
expr_stmt|;
name|classToPrimitive
operator|.
name|put
argument_list|(
name|Boolean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"boolean"
argument_list|)
expr_stmt|;
name|classToPrimitive
operator|.
name|put
argument_list|(
name|Float
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"float"
argument_list|)
expr_stmt|;
name|classToPrimitive
operator|.
name|put
argument_list|(
name|Short
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"short"
argument_list|)
expr_stmt|;
name|classToPrimitive
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"int"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Boolean
name|usePrimitives
decl_stmt|;
comment|/**      * Returns default java.sql.Types type by the Java type name.      *      * @param className Fully qualified Java Class name.      * @return The SQL type or NOT_DEFINED if no type found.      */
specifier|public
name|int
name|getJdbcTypeByJava
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|,
name|String
name|className
parameter_list|)
block|{
if|if
condition|(
name|className
operator|==
literal|null
condition|)
block|{
return|return
name|NOT_DEFINED
return|;
block|}
name|Integer
name|type
init|=
name|javaSqlEnum
operator|.
name|get
argument_list|(
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
return|return
name|type
return|;
block|}
comment|// try to load a Java class - some nonstandard mappings may work
try|try
block|{
return|return
name|getSqlTypeByJava
argument_list|(
name|attribute
argument_list|,
name|Util
operator|.
name|getJavaClass
argument_list|(
name|className
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
return|return
name|NOT_DEFINED
return|;
block|}
block|}
specifier|public
name|void
name|add
parameter_list|(
name|int
name|jdbcType
parameter_list|,
name|String
name|java
parameter_list|)
block|{
name|add
argument_list|(
operator|new
name|DbType
argument_list|(
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|jdbcType
argument_list|)
argument_list|)
argument_list|,
name|java
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|add
parameter_list|(
name|DbType
name|type
parameter_list|,
name|String
name|java
parameter_list|)
block|{
name|mapping
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|java
argument_list|)
expr_stmt|;
name|dbTypes
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Guesses a default JDBC type for the Java class.      *      * @since 1.1      */
specifier|protected
name|int
name|getSqlTypeByJava
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|javaClass
parameter_list|)
block|{
if|if
condition|(
name|javaClass
operator|==
literal|null
condition|)
block|{
return|return
name|NOT_DEFINED
return|;
block|}
comment|// check standard mapping of class and superclasses
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
init|=
name|javaClass
decl_stmt|;
while|while
condition|(
name|aClass
operator|!=
literal|null
condition|)
block|{
name|String
name|name
decl_stmt|;
if|if
condition|(
name|aClass
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|name
operator|=
name|aClass
operator|.
name|getComponentType
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"[]"
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|aClass
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|Object
name|type
init|=
name|javaSqlEnum
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
operator|(
name|Number
operator|)
name|type
operator|)
operator|.
name|intValue
argument_list|()
return|;
block|}
name|aClass
operator|=
name|aClass
operator|.
name|getSuperclass
argument_list|()
expr_stmt|;
block|}
comment|// check non-standard JDBC types that are still supported by JPA
if|if
condition|(
name|javaClass
operator|.
name|isArray
argument_list|()
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|elementType
init|=
name|javaClass
operator|.
name|getComponentType
argument_list|()
decl_stmt|;
if|if
condition|(
name|Character
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|elementType
argument_list|)
operator|||
name|Character
operator|.
name|TYPE
operator|.
name|isAssignableFrom
argument_list|(
name|elementType
argument_list|)
condition|)
block|{
return|return
name|Types
operator|.
name|VARCHAR
return|;
block|}
if|else if
condition|(
name|Byte
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|elementType
argument_list|)
operator|||
name|Byte
operator|.
name|TYPE
operator|.
name|isAssignableFrom
argument_list|(
name|elementType
argument_list|)
condition|)
block|{
return|return
name|Types
operator|.
name|VARBINARY
return|;
block|}
block|}
if|if
condition|(
name|Calendar
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|javaClass
argument_list|)
condition|)
block|{
return|return
name|Types
operator|.
name|TIMESTAMP
return|;
block|}
if|if
condition|(
name|BigInteger
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|javaClass
argument_list|)
condition|)
block|{
return|return
name|Types
operator|.
name|BIGINT
return|;
block|}
if|if
condition|(
name|Serializable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|javaClass
argument_list|)
condition|)
block|{
comment|// serializable check should be the last one when all other mapping attempts failed
return|return
name|Types
operator|.
name|VARBINARY
return|;
block|}
return|return
name|NOT_DEFINED
return|;
block|}
comment|/**      * Get the corresponding Java type by its java.sql.Types counterpart. Note that this      * method should be used as a last resort, with explicit mapping provided by user used      * as a first choice, as it can only guess how to map certain types, such as NUMERIC,      * etc.      *      * @return Fully qualified Java type name or null if not found.      */
annotation|@
name|Override
specifier|public
name|String
name|getJavaByJdbcType
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|,
name|int
name|type
parameter_list|)
block|{
name|String
name|jdbcType
init|=
name|TypesMapping
operator|.
name|getSqlNameByType
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|DbType
name|dbType
decl_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
name|dbType
operator|=
operator|new
name|DbType
argument_list|(
name|jdbcType
argument_list|,
name|attribute
operator|.
name|getMaxLength
argument_list|()
argument_list|,
name|attribute
operator|.
name|getAttributePrecision
argument_list|()
argument_list|,
name|attribute
operator|.
name|getScale
argument_list|()
argument_list|,
name|attribute
operator|.
name|isMandatory
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dbType
operator|=
operator|new
name|DbType
argument_list|(
name|jdbcType
argument_list|)
expr_stmt|;
block|}
name|String
name|typeName
init|=
name|getJavaByJdbcType
argument_list|(
name|dbType
argument_list|)
decl_stmt|;
if|if
condition|(
name|usePrimitives
operator|!=
literal|null
operator|&&
name|usePrimitives
condition|)
block|{
name|String
name|primitive
init|=
name|classToPrimitive
operator|.
name|get
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|primitive
operator|!=
literal|null
condition|)
block|{
return|return
name|primitive
return|;
block|}
block|}
return|return
name|typeName
return|;
block|}
specifier|public
name|String
name|getJavaByJdbcType
parameter_list|(
name|DbType
name|type
parameter_list|)
block|{
for|for
control|(
name|DbType
name|t
range|:
name|dbTypes
control|)
block|{
if|if
condition|(
name|t
operator|.
name|isCover
argument_list|(
name|type
argument_list|)
condition|)
block|{
comment|// because dbTypes sorted by specificity we will take first and the most specific matching
comment|// that applicable for attribute
return|return
name|mapping
operator|.
name|get
argument_list|(
name|t
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Boolean
name|getUsePrimitives
parameter_list|()
block|{
return|return
name|usePrimitives
return|;
block|}
specifier|public
name|void
name|setUsePrimitives
parameter_list|(
name|Boolean
name|usePrimitives
parameter_list|)
block|{
name|this
operator|.
name|usePrimitives
operator|=
name|usePrimitives
expr_stmt|;
block|}
block|}
end_class

end_unit

