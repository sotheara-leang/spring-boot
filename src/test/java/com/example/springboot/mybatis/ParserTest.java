package com.example.springboot.mybatis;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.assertj.core.util.Lists;
import org.junit.Test;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.update.Update;

public class ParserTest {

	@Test
	public void testXMLConfigBuilder() throws IOException {
		Reader configReader = Resources.getResourceAsReader( "mybatis/sqlmap-config.xml" );

		XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder( configReader );

		Configuration configuration = xmlConfigBuilder.parse();

		System.out.println( configuration );
	}

	@Test
	public void testXMLMapperBuilder() throws IOException {
		Configuration configuration = new Configuration();

		String resource = "mybatis/CustomerInfoEbc.xml";

		InputStream inputStream = Resources.getResourceAsStream( resource );

		XMLMapperBuilder builder = new XMLMapperBuilder( inputStream, configuration, resource,
				configuration.getSqlFragments() );

		builder.parse();

		MappedStatement mappedStatement = configuration
				.getMappedStatement( "ppcb.apsara.pcm.pcu.ebi.CustomerInfoEbc.regtCustomerInfo" );

		// in case for each : param need to be hashmap

		Object param = null;

		BoundSql boundSql = mappedStatement.getBoundSql( param );

		System.out.println( boundSql.getSql() );
	}

	@Test
	public void testSQLParser() throws IOException, JSQLParserException {
		Configuration configuration = new Configuration();

		String resource = "mybatis/CustomerInfoEbc.xml";

		InputStream inputStream = Resources.getResourceAsStream( resource );

		XMLMapperBuilder builder = new XMLMapperBuilder( inputStream, configuration, resource,
				configuration.getSqlFragments() );

		builder.parse();

		MappedStatement mappedStatement = configuration
				.getMappedStatement( "ppcb.apsara.pcm.pcu.ebi.CustomerInfoEbc.inquiryCustomerInfoByCustomerNo2" );

		// result maps
		List<ResultMap> resultMaps = mappedStatement.getResultMaps();
		if ( resultMaps != null ) {
			for ( ResultMap resultMap : resultMaps ) {
				Set<String> mappedColumns = resultMap.getMappedColumns();
				if ( !mappedColumns.isEmpty() ) {
					String column = Lists.newArrayList( mappedColumns ).get( 0 );
					System.out.println( column );
				}

				Set<String> mappedProperties = resultMap.getMappedProperties();
				if ( !mappedProperties.isEmpty() ) {
					String property = Lists.newArrayList( mappedProperties ).get( 0 );
					System.out.println( property );
				}
			}
		}

		// bound sql
		BoundSql boundSql = mappedStatement.getBoundSql( null );

		// sql parameters
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		for ( ParameterMapping parameterMapping : parameterMappings ) {
			Class<?> javaType = parameterMapping.getJavaType();
			JdbcType jdbcType = parameterMapping.getJdbcType();
			ParameterMode mode = parameterMapping.getMode();

			System.out.println(
					String.format( "Parameter: javaType=%s, jdbcType=%s, mode=%s", javaType, jdbcType, mode ) );
		}

		Statements stmt = CCJSqlParserUtil.parseStatements( boundSql.getSql() );

		List<Statement> statements = stmt.getStatements();

		for ( Statement statement : statements ) {

			// Select
			if ( statement instanceof Select ) {
				Select select = (Select) statement;

				PlainSelect plainSelect = (PlainSelect) select.getSelectBody();;

				List<SelectItem> selectItems = plainSelect.getSelectItems();
				for ( SelectItem selectItem : selectItems ) {

					// in case * => AllColumns

					if ( selectItem instanceof SelectExpressionItem ) {
						SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;

						// alias
						String aliasName = null;
						Alias alias = selectExpressionItem.getAlias();
						if ( alias != null ) {
							aliasName = alias.getName();
						}

						// column name
						Column column = (Column) selectExpressionItem.getExpression();
						String columnName = column.getColumnName();

						System.out.println( aliasName );
						System.out.println( columnName );
					}
				}

				// Insert
			} else if ( statement instanceof Insert ) {
				Insert insert = (Insert) statement;

				Table table = insert.getTable();
				String tableName = table.getName();

				List<Column> columns = insert.getColumns();

				System.out.println( tableName );
				System.out.println( columns );

				// Update
			} else if ( statement instanceof Update ) {
				Update update = (Update) statement;

				List<Table> tables = update.getTables();
				List<Column> columns = update.getColumns();

				System.out.println( tables );
				System.out.println( columns );

				// Delete
			} else if ( statement instanceof Delete ) {
				Delete delete = (Delete) statement;

				Table table = delete.getTable();
				String tableName = table.getName();

				System.out.println( tableName );
			}
		}
	}
}
