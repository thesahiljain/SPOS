%{
extern int yyerror(char* str);
#include "y.tab.h"
%}

%%

"int" return DT;
"," return COMMA;
";" return SC;
"\n" return NL;
[0-9]* return NUM;
[a-z|A-Z][a-z|A-Z|0-9]* return ID;

%%

int yywrap(){
  return 1;
}
