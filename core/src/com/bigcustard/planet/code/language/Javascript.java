package com.bigcustard.planet.code.language;

import com.bigcustard.planet.code.language.Language;
import com.bigcustard.planet.language.JavascriptKeywords;

import java.util.HashSet;
import java.util.Set;

public class Javascript extends Language {
    public static final String TEMPLATE =
              "///////////////////////////////////// \n"
            + "//       Welcome to GLIDE!         // \n"
            + "//  Start writing your game below  // \n"
            + "// Look at Samples for inspiration // \n"
            + "///////////////////////////////////// \n\n";

    public Javascript() {
        super(new JavascriptKeywords(), "js", "javascript-button", TEMPLATE);
    }

    @Override
    public Set<Integer> errorLineChecker(String code) {
        return new HashSet<>();
    }
}
