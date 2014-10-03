var lineup = new function() {
    /**
     * Should return a css position string such as 'left:50px;top:100px;'
     *
     * "position":
     * 	{"id":128,"name":"Innermitt (off)","code":"IM (O)","side":"CENTRAL","positionType":
 * 		{"id":114,"name":"Mittfältare","alignment":"MF"},
 * "minorVerticalAlignment":"OFFENSIVE","minorHorizontalAlignment":"NEUTRAL"}}
     * @param position
     */

    var TOTAL_Y_OFFSET = 10;

    this.getPositionOffset = function(position) {
        // First, get base vertical position (GK, DEF, MF, FW)
        var top = 0;
        var left = 0;
        switch(position.positionType.alignment) {
            case 'GK':
                top = 40;
                break;
            case 'DEF':
                top = 120;
                break;
            case 'MF':
                top = 230;
                break;
            case 'FW':
                top = 330;
                break;
        }

        switch(position.side) {
            case 'RIGHT':
                left = 100;
                break;
            case 'CENTRAL':
                left = 320;
                break;
            case 'LEFT':
                left = 550;
                break;
        }

        switch(position.minorVerticalAlignment) {
            case 'DEFENSIVE':
                top -= 30;
                break;
            case 'OFFENSIVE':
                top += 40;
                break;
        }

        switch(position.minorHorizontalAlignment) {
            case 'LEFT':
                left += 90;
                break;
            case 'RIGHT':
                left -= 90;
                break;
        }

        return 'left:' + (left-40) + 'px;top:' + (top-TOTAL_Y_OFFSET) + 'px;';
    }
}