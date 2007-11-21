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
name|map
package|;
end_package

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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|CayenneMapEntry
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|XMLEncoder
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
name|XMLSerializable
import|;
end_import

begin_comment
comment|/**  * A mapping descriptor for a database stored procedure.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|Procedure
implements|implements
name|CayenneMapEntry
implements|,
name|XMLSerializable
implements|,
name|Serializable
block|{
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|protected
name|String
name|catalog
decl_stmt|;
specifier|protected
name|String
name|schema
decl_stmt|;
specifier|protected
name|boolean
name|returningValue
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ProcedureParameter
argument_list|>
name|callParameters
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcedureParameter
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Creates an unnamed procedure object.      */
specifier|public
name|Procedure
parameter_list|()
block|{
block|}
comment|/**      * Creates a named Procedure object.      */
specifier|public
name|Procedure
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Object
name|getParent
parameter_list|()
block|{
return|return
name|getDataMap
argument_list|()
return|;
block|}
specifier|public
name|void
name|setParent
parameter_list|(
name|Object
name|parent
parameter_list|)
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|parent
operator|instanceof
name|DataMap
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected null or DataMap, got: "
operator|+
name|parent
argument_list|)
throw|;
block|}
name|setDataMap
argument_list|(
operator|(
name|DataMap
operator|)
name|parent
argument_list|)
expr_stmt|;
block|}
comment|/**      * Prints itself as XML to the provided XMLEncoder.      *       * @since 1.1      */
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<procedure name=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|Util
operator|.
name|encodeXmlAttribute
argument_list|(
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|'\"'
argument_list|)
expr_stmt|;
if|if
condition|(
name|getSchema
argument_list|()
operator|!=
literal|null
operator|&&
name|getSchema
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" schema=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|getSchema
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|'\"'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getCatalog
argument_list|()
operator|!=
literal|null
operator|&&
name|getCatalog
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" catalog=\""
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|getCatalog
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
literal|'\"'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isReturningValue
argument_list|()
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" returningValue=\"true\""
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|println
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|indent
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|print
argument_list|(
name|getCallParameters
argument_list|()
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|indent
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"</procedure>"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns procedure name including schema, if present.      */
specifier|public
name|String
name|getFullyQualifiedName
parameter_list|()
block|{
return|return
operator|(
name|schema
operator|!=
literal|null
operator|)
condition|?
name|schema
operator|+
literal|'.'
operator|+
name|getName
argument_list|()
else|:
name|getName
argument_list|()
return|;
block|}
comment|/**      * @return parent DataMap of this entity.      */
specifier|public
name|DataMap
name|getDataMap
parameter_list|()
block|{
return|return
name|dataMap
return|;
block|}
comment|/**      * Sets parent DataMap of this entity.      */
specifier|public
name|void
name|setDataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
block|}
specifier|public
name|void
name|setCallParameters
parameter_list|(
name|List
argument_list|<
name|ProcedureParameter
argument_list|>
name|parameters
parameter_list|)
block|{
name|clearCallParameters
argument_list|()
expr_stmt|;
name|callParameters
operator|.
name|addAll
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds new call parameter to the stored procedure. Also sets<code>param</code>'s      * parent to be this procedure.      */
specifier|public
name|void
name|addCallParameter
parameter_list|(
name|ProcedureParameter
name|param
parameter_list|)
block|{
if|if
condition|(
name|param
operator|.
name|getName
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Attempt to add unnamed parameter."
argument_list|)
throw|;
block|}
if|if
condition|(
name|callParameters
operator|.
name|contains
argument_list|(
name|param
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Attempt to add the same parameter more than once:"
operator|+
name|param
argument_list|)
throw|;
block|}
name|param
operator|.
name|setProcedure
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|callParameters
operator|.
name|add
argument_list|(
name|param
argument_list|)
expr_stmt|;
block|}
comment|/** Removes a named call parameter. */
specifier|public
name|void
name|removeCallParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callParameters
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ProcedureParameter
name|nextParam
init|=
name|callParameters
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|nextParam
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|callParameters
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
specifier|public
name|void
name|clearCallParameters
parameter_list|()
block|{
name|callParameters
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns an unmodifiable list of call parameters.      */
specifier|public
name|List
argument_list|<
name|ProcedureParameter
argument_list|>
name|getCallParameters
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|callParameters
argument_list|)
return|;
block|}
comment|/**      * Returns a list of OUT and INOUT call parameters. If procedure has a return value,      * it will also be included as a call parameter.      */
specifier|public
name|List
argument_list|<
name|ProcedureParameter
argument_list|>
name|getCallOutParameters
parameter_list|()
block|{
name|List
argument_list|<
name|ProcedureParameter
argument_list|>
name|outParams
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcedureParameter
argument_list|>
argument_list|(
name|callParameters
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ProcedureParameter
name|param
range|:
name|callParameters
control|)
block|{
if|if
condition|(
name|param
operator|.
name|isOutParam
argument_list|()
condition|)
block|{
name|outParams
operator|.
name|add
argument_list|(
name|param
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|outParams
return|;
block|}
comment|/**      * Returns parameter describing the return value of the StoredProcedure, or null if      * procedure does not support return values. If procedure supports return parameters,      * its first parameter is always assumed to be a return result.      */
specifier|public
name|ProcedureParameter
name|getResultParam
parameter_list|()
block|{
comment|// if procedure returns parameters, this must be the first parameter
comment|// otherwise, return null
return|return
operator|(
name|returningValue
operator|&&
name|callParameters
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|)
condition|?
operator|(
name|ProcedureParameter
operator|)
name|callParameters
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
comment|/**      * Returns<code>true</code> if a stored procedure returns a value. The first      * parameter in a list of parameters will be assumed to be a descriptor of return      * value.      *       * @return boolean      */
specifier|public
name|boolean
name|isReturningValue
parameter_list|()
block|{
return|return
name|returningValue
return|;
block|}
specifier|public
name|void
name|setReturningValue
parameter_list|(
name|boolean
name|returningValue
parameter_list|)
block|{
name|this
operator|.
name|returningValue
operator|=
name|returningValue
expr_stmt|;
block|}
specifier|public
name|String
name|getCatalog
parameter_list|()
block|{
return|return
name|catalog
return|;
block|}
specifier|public
name|String
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
comment|/**      * Sets stored procedure's catalog.      */
specifier|public
name|void
name|setCatalog
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|catalog
operator|=
name|string
expr_stmt|;
block|}
comment|/**      * Sets stored procedure's database schema.      */
specifier|public
name|void
name|setSchema
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|schema
operator|=
name|string
expr_stmt|;
block|}
block|}
end_class

end_unit

