
const defaultPercentColors = [
    { percentage: 0.0, color: { r: 0x00, g: 0xff, b: 0 } },
    { percentage: 0.5, color: { r: 0xff, g: 0xff, b: 0 } },
    { percentage: 1.0, color: { r: 0xff, g: 0x00, b: 0 } }
];

function getColorForPercentage(percentage, percentColors) {
    if (typeof percentColors == "undefined") {
        percentColors = defaultPercentColors;
    }

    for (var i = 1; i < percentColors.length - 1; i++) {
        if (percentage < percentColors[i].percentage) {
            break;
        }
    }
    var lower = percentColors[i - 1];
    var upper = percentColors[i];
    var range = upper.percentage - lower.percentage;
    var rangePct = (percentage - lower.percentage) / range;
    var percentageLower = 1 - rangePct;
    var percentageUpper = rangePct;
    var color = {
        r: Math.floor(lower.color.r * percentageLower + upper.color.r * percentageUpper),
        g: Math.floor(lower.color.g * percentageLower + upper.color.g * percentageUpper),
        b: Math.floor(lower.color.b * percentageLower + upper.color.b * percentageUpper)
    };
    return "rgb(" + [color.r, color.g, color.b].join(",") + ")";
}
